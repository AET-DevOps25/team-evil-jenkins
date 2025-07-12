import React, { useState } from 'react';
import Header from '../components/Header';
import '../styles/MatchingPage.css';

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

function MatchingPage() {
  const [view, setView] = useState('cards');

  return (
    <>
      <Header isAuth />
      <section className="matching-page container">
        {/* Filters */}
        <div className="filters card">
          <div className="filter-group">
            <div className="filter-item">
              <label htmlFor="loc">Location</label>
              <select id="loc" defaultValue="San Francisco, CA">
                <option>San Francisco, CA</option>
                <option>Los Angeles, CA</option>
              </select>
          </div>

          <div className="filter-item">
            <label>Sports</label>
            <div className="tags">
              <span className="tag selected">Running</span>
              <span className="tag selected">Cycling</span>
              <input type="text" placeholder="Add sport" className="input-xs" />
            </div>
          </div>

          <div className="filter-item">
            <label>Availability</label>
            <select defaultValue="Weekday Mornings">
              <option>Weekday Mornings</option>
              <option>Weekday Evenings</option>
              <option>Weekends</option>
            </select>
          </div>

          </div>

          <div className="actions">
            <div className="filter-item view-toggle">
              <button
                className={`btn-xs ${view === 'cards' ? 'active' : ''}`}
                onClick={() => setView('cards')}
              >
                📇 Cards
              </button>
              <button
                className={`btn-xs ${view === 'map' ? 'active' : ''}`}
                onClick={() => setView('map')}
              >
                🗺️ Map
              </button>
            </div>
            <button className="btn btn-primary match-btn">Match</button>
          </div>
        </div>

        {/* Matches list */}
        {view === 'cards' && (
          <>
            <div className="matches-header">
              <h3>Top Matches for You</h3>
              <button className="btn-link edit">⚙️ Edit Preferences</button>
            </div>
            <div className="match-grid">
              {mockMatches.map((m) => (
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
                  <button className="btn btn-secondary full send">💬 Send Message</button>
                </div>
              ))}
            </div>
            <div className="load-more-wrapper">
              <button className="btn btn-outline">Load More Matches</button>
            </div>
          </>
        )}
        {view === 'map' && (
          <div className="map-placeholder card">Map view coming soon…</div>
        )}
      </section>
    </>
  );
}

export default MatchingPage;
