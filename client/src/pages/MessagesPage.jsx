import React, { useState } from 'react';
import Header from '../components/Header';
import '../styles/MessagesPage.css';

const sampleConversations = [
    {
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
];

const MessagesPage = () => {
    const [conversations, setConversations] = useState(sampleConversations);
    const [selectedId, setSelectedId] = useState(sampleConversations[0].id);
    const [input, setInput] = useState('');

    const selected = conversations.find((c) => c.id === selectedId);

    const handleSend = () => {
        if (!input.trim()) return;
        const newMsg = {
            from: 'me',
            text: input.trim(),
            time: new Date().toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' }),
        };
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
                        <input placeholder="Search conversations..." />
                    </div>
                    <ul className="conversation-list">
                        {conversations.map((c) => (
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
