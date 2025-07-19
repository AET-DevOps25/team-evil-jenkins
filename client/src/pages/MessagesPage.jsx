import React, { useState, useEffect, useRef } from 'react';
import Header from '../components/Header';
import '../styles/MessagesPage.css';
import { useAuth0 } from '@auth0/auth0-react';
import { useNotification } from '../contexts/NotificationContext';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
/*
       id: 1,
       name: 'Max Johnson',
       avatar: '/images/avatar2.png',
       last: "Great! Let's meet at Golden Gate Park at 7 AM",
       lastTime: '2m',
       messages: [
           {
               from: 'them',
               text: 'Hey Lena! I saw we matched for running. Are you free tomorrow morning?',
               time: '9:30 AM',
           },
           {
               from: 'me',
               text: "Hi Max! Yes, I'd love to go for a run. What time works for you?",
               time: '9:32 AM',
           },
           {
               from: 'them',
               text: "Great! Let's meet at Golden Gate Park at 7 AM. I know a nice 5K route.",
               time: '9:35 AM',
           },
           {
               from: 'me',
               text: 'Perfect! See you there at 7 AM sharp 🏃‍♂️',
               time: '9:36 AM',
           },
       ],
   },
   {
       id: 2,
       name: 'Sarah Chen',
       avatar: '/images/avatar3.png',
       last: 'The hiking trail was amazing yesterday!',
       lastTime: '1h',
       messages: [],
   },
   {
       id: 3,
       name: 'SF Runners Group',
       avatar: '/images/icon-group.png',
       last: "Who's joining tomorrow's marathon training?",
       lastTime: '3h',
       messages: [],
   },
*/

// API URL configuration for different environments
// Docker: Frontend on :3000, nginx gateway on :80
// Kubernetes: Frontend and API on separate domains
const API_URL = (window.location.hostname === 'localhost' ? 'http://localhost:80' : `https://api.${window.location.hostname}`);

const MessagesPage = () => {
    const [conversations, setConversations] = useState([]);
    const [selectedId, setSelectedId] = useState(null);
    const [input, setInput] = useState('');
    const [searchQuery, setSearchQuery] = useState('');
    const { user, getAccessTokenSilently } = useAuth0();
    const { notify } = useNotification();
    const stompClientRef = useRef(null);
    const subscriptionsRef = useRef(new Map());

    const selected = conversations.find((c) => c.id === selectedId);

    // Filter conversations based on search query
    const filteredConversations = conversations.filter((c) =>
        c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        c.last.toLowerCase().includes(searchQuery.toLowerCase())
    );

    // WebSocket connection setup
    useEffect(() => {
        if (!user) return;

        const connectWebSocket = async () => {
            try {
                const token = await getAccessTokenSilently();

                const client = new Client({
                    webSocketFactory: () => {
                        const sockjs = new SockJS(`${API_URL}/ws`);
                        return sockjs;
                    },
                    connectHeaders: {
                        Authorization: `Bearer ${token}`
                    },
                    debug: (str) => console.log('STOMP Debug:', str),
                    reconnectDelay: 5000,
                    heartbeatIncoming: 4000,
                    heartbeatOutgoing: 4000,
                    // Skip credentials for initial handshake
                    forceBinaryWSFrames: false,
                    appendMissingNULLonIncoming: true,
                });

                client.onConnect = () => {
                    console.log('WebSocket connected');
                    stompClientRef.current = client;
                };

                client.onStompError = (frame) => {
                    console.error('STOMP error:', frame);
                };

                client.onWebSocketError = (error) => {
                    console.error('WebSocket error:', error);
                };

                client.activate();
            } catch (error) {
                console.error('Failed to connect WebSocket:', error);
            }
        };

        connectWebSocket();

        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, [user, getAccessTokenSilently]);

    // Subscribe to conversation updates when selectedId changes
    useEffect(() => {
        if (!selectedId || !user || !stompClientRef.current?.connected) return;

        const conversationId = buildConversationId(user.sub, selectedId);
        const topic = `/topic/conversation.${conversationId}`;

        // Unsubscribe from previous conversation
        const existingSubscription = subscriptionsRef.current.get('current');
        if (existingSubscription) {
            existingSubscription.unsubscribe();
        }

        // Subscribe to new conversation
        const subscription = stompClientRef.current.subscribe(topic, (message) => {
            const newMessage = JSON.parse(message.body);

            // Add the new message to the conversation
            setConversations((prev) =>
                prev.map((c) => {
                    if (c.id === selectedId) {
                        const messageObj = {
                            from: newMessage.fromUserId === user.sub ? 'me' : 'them',
                            text: newMessage.content,
                            time: new Date(newMessage.timestamp).toLocaleTimeString([], {
                                hour: 'numeric',
                                minute: '2-digit'
                            }),
                        };

                        // Check if message already exists to avoid duplicates
                        const messageExists = c.messages.some(m =>
                            m.text === messageObj.text &&
                            m.time === messageObj.time &&
                            m.from === messageObj.from
                        );

                        if (!messageExists) {
                            return {
                                ...c,
                                messages: [...c.messages, messageObj],
                                last: newMessage.content,
                                lastTime: 'now'
                            };
                        }
                    }
                    return c;
                })
            );
        });

        subscriptionsRef.current.set('current', subscription);

        return () => {
            if (subscription) {
                subscription.unsubscribe();
                subscriptionsRef.current.delete('current');
            }
        };
    }, [selectedId, user]);

    // Helper function to build conversation ID (same as backend)
    const buildConversationId = (userA, userB) => {
        return userA.localeCompare(userB) < 0 ? `${userA}-${userB}` : `${userB}-${userA}`;
    };

    // Load contacts on mount
    React.useEffect(() => {
        const loadContacts = async () => {
            try {
                const token = await getAccessTokenSilently();
                const response = await fetch(`${API_URL}/messaging/contacts/${encodeURIComponent(user.sub)}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                if (!response.ok) throw new Error(`Contacts fetch failed: ${response.status}`);
                const contacts = await response.json(); // array of UserDTO
                // Convert to conversation objects
                const mapped = contacts.map((u) => ({
                    id: u.id,
                    name: `${u.firstName ?? ''} ${u.lastName ?? ''}`.trim() || u.name || u.id,
                    avatar: u.avatarUrl || '/images/avatar2.png',
                    last: '',
                    lastTime: '',
                    messages: [],
                }));
                setConversations(mapped);
                if (mapped.length) setSelectedId(mapped[0].id);
            } catch (e) {
                console.error(e);
                notify('Failed to load contacts', 'error');
            }
        };
        if (user) loadContacts();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [user]);

    // Load conversation when selectedId changes
    React.useEffect(() => {
        const loadConversation = async () => {
            if (!selectedId) return;
            try {
                const token = await getAccessTokenSilently();
                const url = new URL(`${API_URL}/messaging/conversation`);
                url.searchParams.append('userA', user.sub);
                url.searchParams.append('userB', selectedId);
                url.searchParams.append('page', '0');
                url.searchParams.append('size', '100');
                const response = await fetch(url, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                if (!response.ok) throw new Error('conv fetch');
                const page = await response.json();
                const msgs = page.content || [];
                setConversations((prev) =>
                    prev.map((c) => (c.id === selectedId ? {
                        ...c, messages: msgs.map((m) => ({
                            from: m.fromUserId === user.sub ? 'me' : 'them',
                            text: m.content,
                            time: new Date(m.timestamp).toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' }),
                        }))
                    } : c))
                );
            } catch (err) {
                console.error(err);
                notify('Failed to load conversation', 'error');
            }
        };
        if (user) loadConversation();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedId]);

    const handleSend = async () => {
        if (!input.trim()) return;
        const newMsg = {
            from: 'me',
            text: input.trim(),
            time: new Date().toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' }),
        };
        // optimistic update
        setConversations((prev) =>
            prev.map((c) =>
                c.id === selectedId
                    ? {
                        ...c,
                        messages: [...c.messages, newMsg],
                        last: input.trim(),
                        lastTime: 'now',
                    }
                    : c
            )
        );
        setInput('');

        // POST to backend
        try {
            const token = await getAccessTokenSilently();
            await fetch(`${API_URL}/messaging/send`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    fromUserId: user.sub,
                    toUserId: selectedId,
                    content: newMsg.text,
                }),
            });
        } catch (err) {
            console.error(err);
            notify('Failed to send message', 'error');
        }
    };

    return (
        <>
            <Header />
            <div className="messages-container">
                <aside className="sidebar">
                    <div className="sidebar-header">
                        <h2>Messages</h2>
                        <button className="icon-btn">+</button>
                    </div>
                    <div className="search">
                        <input
                            placeholder="Search conversations..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <ul className="conversation-list">
                        {filteredConversations.map((c) => (
                            <li
                                key={c.id}
                                className={`conversation-item ${c.id === selectedId ? 'active' : ''}`}
                                onClick={() => setSelectedId(c.id)}
                            >
                                <img src={c.avatar} alt="" className="avatar" />
                                <div className="info">
                                    <span className="name">{c.name}</span>
                                    <span className="snippet">{c.last}</span>
                                </div>
                                <span className="time">{c.lastTime}</span>
                            </li>
                        ))}
                    </ul>
                </aside>
                <main className="chat-area">
                    {selected && (
                        <>
                            <div className="chat-header">
                                <img src={selected.avatar} alt="" className="avatar-lg" />
                                <div className="details">
                                    <h3>{selected.name}</h3>
                                    <span className="status online">Online</span>
                                </div>
                            </div>

                            <div className="message-list">
                                {selected.messages.map((m, i) => (
                                    <div key={i} className={`message ${m.from === 'me' ? 'sent' : 'received'}`}>
                                        <p>{m.text}</p>
                                        <span className="time">{m.time}</span>
                                    </div>
                                ))}
                            </div>

                            <div className="message-input">
                                <input
                                    type="text"
                                    placeholder="Type a message..."
                                    value={input}
                                    onChange={(e) => setInput(e.target.value)}
                                    onKeyDown={(e) => {
                                        if (e.key === 'Enter') handleSend();
                                    }}
                                />
                                <button onClick={handleSend} className="icon-btn send-btn">
                                    ➤
                                </button>
                            </div>
                        </>
                    )}
                </main>
            </div>
        </>
    );
};

export default MessagesPage;
