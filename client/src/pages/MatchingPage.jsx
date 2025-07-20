import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import '../styles/MatchingPage.css';
import { useNotification } from '../contexts/NotificationContext';

const mockMatches = [
  {
    id: 1,
    name: 'Alex Chen',
    distance: 2.5,
    avatar: '/images/avatar1.png',
    sports: ['Running', 'Cycling'],
    shared: 'Marathon training, Weekend rides',
    match: 94,
  },
  {
    id: 2,
    name: 'Sarah Miller',
    distance: 1.8,
    avatar: '/images/avatar2.png',
    sports: ['CrossFit', 'Hiking'],
    shared: 'Morning workouts, Trail running',
    match: 89,
  },
  {
    id: 3,
    name: 'Mike Johnson',
    distance: 3.2,
    avatar: '/images/avatar3.png',
    sports: ['Basketball', 'Tennis'],
    shared: 'Evening games, Weekend matches',
    match: 92,
  },
  {
    id: 4,
    name: 'Emma Davis',
    distance: 1.5,
    avatar: '/images/avatar4.png',
    sports: ['Swimming', 'Yoga'],
    shared: 'Pool sessions, Mindful movement',
    match: 87,
  },
  {
    id: 5,
    name: 'David Wilson',
    distance: 4.1,
    avatar: '/images/avatar5.png',
    sports: ['Rock Climbing', 'Hiking'],
    shared: 'Outdoor adventures, Weekend trips',
    match: 91,
  },
  {
    id: 6,
    name: 'Lisa Thompson',
    distance: 2.9,
    avatar: '/images/avatar6.png',
    sports: ['Badminton', 'Volleyball'],
    shared: 'Team sports, Social games',
    match: 85,
  },
];

// API URL configuration for different environments
// Docker: Frontend on :3000, nginx gateway on :80
// Kubernetes: Frontend and API on separate domains
const API_URL = (window.location.hostname === 'localhost' ? 'http://localhost:80' : `https://api.${window.location.hostname}`);
const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

// simple emoji map for popular sports
const sportEmojis = {
  Hiking: '🥾',
  Running: '🏃',
  Cycling: '🚴',
  Swimming: '🏊',
  Tennis: '🎾',
  Basketball: '🏀',
  Soccer: '⚽',
  Baseball: '⚾',
  Skiing: '⛷️',
  Snowboarding: '🏂',
  Skateboarding: '🛹',
  Surfing: '🏄',
  Rowing: '🚣',
  Boxing: '🥊',
  'Martial Arts': '🥋',
  Climbing: '🧗',
  Golf: '⛳',
  Dancing: '💃',
  Yoga: '🧘',
  Pilates: '🧘',
  CrossFit: '💪',
  Weightlifting: '🏋️',
  Badminton: '🏸',
  'Table Tennis': '🏓',
  'Horseback Riding': '🏇',
  Fencing: '🤺',
};
const getSportLabel = (s) => `${sportEmojis[s] || '🏅'} ${s}`;

function MatchingPage() {
  const { user, getAccessTokenSilently } = useAuth0();
  const { notify } = useNotification();
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [location, setLocation] = useState('');
  const [matches, setMatches] = useState([]);
  const [matchingLoading, setMatchingLoading] = useState(false);
  const [showExplanationModal, setShowExplanationModal] = useState(false);
  const [selectedExplanation, setSelectedExplanation] = useState(null);

  // validation helpers and Match click handler
  const hasValidAvailability = (avl) => avl && Object.values(avl).some((arr) => Array.isArray(arr) && arr.length);

  const handleMatch = () => {
    setMatchingLoading(true);
    if (!profile) {
      notify({ type: 'error', message: 'Profile data not loaded yet. Please wait a moment.' });
      return;
    }
    if (!location) {
      notify({ type: 'error', message: 'Location missing. Please enable location sharing and set it in your profile.' });
      return;
    }
    if (!profile.sports || profile.sports.length === 0) {
      notify({ type: 'error', message: 'Please add at least one sport in your profile before matching.' });
      return;
    }
    if (!profile.skillLevel) {
      notify({ type: 'error', message: 'Please set your skill level in your profile.' });
      return;
    }
    if (!hasValidAvailability(profile.availability)) {
      notify({ type: 'error', message: 'Please specify your availability (at least one day and time slot).' });
      return;
    }
    // all good – fetch matches from backend
    (async () => {
      try {
        const token = await getAccessTokenSilently();
        // trigger fresh matching run
        const partnersRes = await fetch(`${API_URL}/matching/partners/${encodeURIComponent(user.sub)}`, {
          method: 'POST',

          headers: { Authorization: `Bearer ${token}` },
        });
        if (!partnersRes.ok) {
          notify({ type: 'error', message: 'Failed to fetch partners' });
          return;
        }
        const users = await partnersRes.json();
        // fetch scores/history to get match percentage & explanation
        const historyRes = await fetch(`${API_URL}/matching/history/${encodeURIComponent(user.sub)}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        let history = [];
        if (historyRes.ok) {
          history = await historyRes.json();
        }
        const scoreMap = Object.fromEntries(history.map(h => [h.matchedUserId, h.score]));
        const explanationMap = Object.fromEntries(history.map(h => [h.matchedUserId, h.explanation]));
        const commonMap = Object.fromEntries(history.map(h => [h.matchedUserId, h.commonPreferences]));
        const formatted = users.map(u => ({
          id: u.id,
          name: u.name,
          distance: 0, // TODO backend distance
          avatar: u.picture || '/images/default-avatar.png',
          sports: u.sportInterests || [],
          shared: commonMap[u.id]?.join(', ') || '',
          match: scoreMap[u.id] ? Math.round(scoreMap[u.id] * 100) : 0,
          explanation: explanationMap[u.id],
        }));
        setMatches(formatted);
        setMatchingLoading(false);
      } catch (e) {
        console.error('match fetch failed', e);
        notify({ type: 'error', message: 'Unable to retrieve matches. Try again later.' });
        setMatchingLoading(false);
      }
    })();
  };

  // Show explanation modal
  const handleShowExplanation = (match) => {
    setSelectedExplanation({
      name: match.name,
      explanation: match.explanation,
      match: match.match
    });
    setShowExplanationModal(true);
  };

  // add contact and redirect to messages
  const handleSendMessage = async (match) => {
    try {
      const token = await getAccessTokenSilently();
      const response = await fetch(`${API_URL}/messaging/contact?userId=${encodeURIComponent(user.sub)}&contactId=${encodeURIComponent(match.id)}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.ok) {
        notify({ type: 'success', message: `Contact ${match.name} added! You can now chat.` });
        navigate('/messages');
      } else {
        notify({ type: 'error', message: 'Failed to add contact. Please try again.' });
      }
    } catch (err) {
      console.error(err);
      notify({ type: 'error', message: 'Failed to add contact.' });
    }
  };

  const [view, setView] = useState('cards');

  useEffect(() => {
    if (!user) return;
    (async () => {
      try {
        const token = await getAccessTokenSilently();
        // fetch profile
        const res = await fetch(`${API_URL}/user/${encodeURIComponent(user.sub)}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setProfile({
            sports: data.sportInterests || [],
            skillLevel: data.skillLevel || '',
            availability: data.availability || {},
          });
        }
        // fetch location
        const locRes = await fetch(`${API_URL}/location/${encodeURIComponent(user.sub)}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (locRes.ok) {
          const loc = await locRes.json();
          if (!loc.latitude || !loc.longitude) {
            console.error('location not found', loc);
            notify({ type: 'error', message: 'Location not found. Make sure you have enabled location sharing in your browser settings.' });
            return;
          }
          try {
            const resp = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${loc.latitude}&lon=${loc.longitude}&format=json`);
            if (resp.ok) {
              const info = await resp.json();
              setLocation(info.display_name || `${loc.latitude}, ${loc.longitude}`);
            } else {
              console.error('reverse geocode failed', resp);
            }
          } catch (e) {
            console.error('reverse geocode failed', e);
          }
        }
      } catch (err) {
        console.error('Failed to load profile for filters', err);
        notify({ type: 'error', message: 'Failed to load profile for filters' });
      }
    })();
  }, [user, getAccessTokenSilently]);


  return (
    <>
      <Header isAuth />
      <section className="matching-page container">
        {/* Filters */}
        <div className="filters card">
          <div className="filters-header">
            <h4>Your Filters</h4>
            <button
              className="btn-link edit"
              title="Edit filters"
              onClick={() => navigate('/profile')}
            >
              Edit Preferences ✏️
            </button>
          </div>
          <div className="filter-group">
            {/* Location */}
            <div className="filter-item">
              <label>Location</label>
              <div className="tags">
                {location ? (
                  <span className="tag location" title={location}><span className="icon">📍</span> {location}</span>
                ) : (
                  <span className="text-muted">Unknown</span>
                )}
              </div>
            </div>

            <div className="filter-item">
              <label>Sports</label>
              <div className="tags">
                {profile?.sports?.length ? (
                  profile.sports.map((s) => (
                    <span key={s} className="tag selected">{getSportLabel(s)}</span>
                  ))
                ) : (
                  <span className="text-muted">None</span>
                )}
              </div>
            </div>

            <div className="filter-item">
              <label>Skill</label>
              <span className="badge skill">{profile?.skillLevel || 'Not set'}</span>
            </div>

            <div className="filter-item">
              <label>Availability</label>
              <div className="availability-grid">
                {profile?.availability && Object.entries(profile.availability)
                  .filter(([, times]) => times.length)
                  .map(([day, times]) => (
                    <div key={day} className="avail-day">
                      <span className="day">{day.substring(0, 3)}</span>
                      {times.map((t) => (
                        <span key={t} className="slot">{t}</span>
                      ))}
                    </div>
                  ))}
                {!profile?.availability && (
                  <span className="text-muted">Not set</span>
                )}
              </div>
            </div>

          </div>

          <div className="actions">

            <button className="btn btn-primary match-btn" onClick={handleMatch}>Match</button>
          </div>
        </div>

        {/* Matches list */}
        {view === 'cards' && (
          matchingLoading ? (
            <div className="match-loader">
              <div className="spinner" />
              <p>Finding your best matches…</p>
            </div>
          ) : (
            <>
              <div className="matches-header">
                <h3>Top Matches for You</h3>
                <button className="btn-link edit">⚙️ Edit Preferences</button>
              </div>
              <div className="match-grid">
                {(matches.length ? matches : mockMatches).map((m) => (
                  <div key={m.id} className="match-card card">
                    <div className="match-header">
                      <img src={m.avatar} alt={m.name} className="avatar-sm" />
                      <div>
                        <h4>{m.name}</h4>
                        <span className="distance">{m.distance} km away</span>
                      </div>
                      <span className="badge">{m.match}% Match</span>
                    </div>
                    <div className="sports-list">
                      {m.sports.map((s) => (
                        <span key={s} className="sport-chip">{s}</span>
                      ))}
                    </div>
                    <p className="shared">Shared interests: {m.shared}</p>
                    <div className="match-actions">
                      {m.explanation && (
                        <button
                          className="btn btn-outline explanation-btn"
                          onClick={() => handleShowExplanation(m)}
                          title="See why you matched"
                        >
                          💡 See Explanation
                        </button>
                      )}
                      <button className="btn btn-secondary send" onClick={() => handleSendMessage(m)}>💬 Send Message</button>
                    </div>
                  </div>
                ))}
              </div>
              <div className="load-more-wrapper">
                <button className="btn btn-outline" disabled={matchingLoading}>Load More Matches</button>
              </div>
            </>
          ))}
        {view === 'map' && (
          <div className="map-placeholder card">Map view coming soon…</div>
        )}
      </section>

      {/* Explanation Modal */}
      {showExplanationModal && selectedExplanation && (
        <div className="modal-overlay" onClick={() => setShowExplanationModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>💡 Match Explanation</h3>
              <button
                className="modal-close"
                onClick={() => setShowExplanationModal(false)}
                aria-label="Close"
              >
                ×
              </button>
            </div>
            <div className="modal-body">
              <div className="match-info">
                <h4>{selectedExplanation.name}</h4>
                <span className="match-score">{selectedExplanation.match}% Match</span>
              </div>
              <div className="explanation-content">
                <p>{selectedExplanation.explanation}</p>
              </div>
            </div>
            <div className="modal-footer">
              <button
                className="btn btn-primary"
                onClick={() => setShowExplanationModal(false)}
              >
                Got it!
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default MatchingPage;