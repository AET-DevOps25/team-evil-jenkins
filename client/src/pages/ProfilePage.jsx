import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import Header from '../components/Header';
import '../styles/ProfilePage.css';

const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
const timeSlots = ['Morning (6-12 PM)', 'Afternoon (12-6 PM)', 'Evening (6-10 PM)'];

const initialState = {
    firstName: 'Lena',
    lastName: 'Martinez',
    location: 'San Francisco, CA',
    bio: 'New to San Francisco and looking for hiking buddies! Love exploring nature trails and staying active.',
    sports: ['Hiking', 'Running', 'Cycling'],
    skillLevel: 'Beginner',
    availability: daysOfWeek.reduce((acc, d) => ({ ...acc, [d]: [] }), {}),
};

const allSports = ['Hiking', 'Running', 'Cycling', 'Swimming', 'Tennis', 'Basketball'];
const skillLevels = ['Beginner', 'Intermediate', 'Advanced'];

const API = import.meta.env.VITE_API_URL || 'http://localhost:80';

function ProfilePage() {
    const { user, getAccessTokenSilently } = useAuth0();
    const [form, setForm] = useState(initialState);
    const [location, setLocation] = useState({ lat: null, lon: null, address: '' });

    // populate with Auth0 data when available
    useEffect(() => {
        if (user) {
            setForm(prev => ({
                ...prev,
                firstName: user.given_name || user.name?.split(' ')[0] || '',
                lastName: user.family_name || user.name?.split(' ').slice(1).join(' ') || '',
                email: user.email || '',
                avatar: user.picture || '',
            }));
        }
    }, [user]);

    // fetch location from backend and reverse-geocode to a human-readable address
    useEffect(() => {
        if (!user) return;
        (async () => {
            try {
                const token = await getAccessTokenSilently();
                const res = await fetch(`${API}/location/${encodeURIComponent(user.sub)}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (!res.ok) return;
                const data = await res.json(); // { latitude, longitude }
                const { latitude, longitude } = data;
                let address = '';
                if (latitude && longitude) {
                    try {
                        const gRes = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`);
                        if (gRes.ok) {
                            const gJson = await gRes.json();
                            const addrObj = gJson.address || {};
                            const street = addrObj.road || addrObj.pedestrian || '';
                            const houseNo = addrObj.house_number || '';
                            const locality = addrObj.city || addrObj.town || addrObj.village || addrObj.municipality || addrObj.county || '';
                            const state = addrObj.state || '';
                            const parts = [];
                            if (street) parts.push(street + (houseNo ? ` ${houseNo}` : ''));
                            if (locality) parts.push(locality);
                            if (state) parts.push(state);
                            const formatted = parts.join(', ').trim();
                            setLocation({ lat: latitude, lon: longitude, address: formatted || address, raw: address });
                            setForm(prev => ({ ...prev, location: formatted || address }));
                        }
                    } catch (_) {
                        // ignore geocode errors
                    }

                }
            } catch (e) {
                // eslint-disable-next-line no-console
                console.error('Failed to load location', e);
            }
        })();
    }, [user]);

    const handleInput = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleCheckbox = (section, value) => {
        setForm((prev) => {
            const current = new Set(prev[section]);
            current.has(value) ? current.delete(value) : current.add(value);
            return { ...prev, [section]: Array.from(current) };
        });
    };

    const toggleTime = (day, slot) => {
        setForm(prev => {
            const current = new Set(prev.availability[day]);
            current.has(slot) ? current.delete(slot) : current.add(slot);
            return { ...prev, availability: { ...prev.availability, [day]: Array.from(current) } };
        });
    };

    const toggleDay = (day) => {
        setForm(prev => {
            const hasAny = prev.availability[day].length > 0;
            return { ...prev, availability: { ...prev.availability, [day]: hasAny ? [] : [...timeSlots] } };
        });
    };



    const handleSave = async (e) => {
        e.preventDefault();
        try {
            const token = await getAccessTokenSilently();
            const body = {
                firstName: form.firstName,
                lastName: form.lastName,
                bio: form.bio,
                skillLevel: form.skillLevel,
                availability: form.availability,
                sports: form.sports,
            };
            const res = await fetch(`${API}/user/${user.sub}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(body),
            });
            if (!res.ok) throw new Error(`Failed: ${res.status}`);
            // eslint-disable-next-line no-alert
            alert('Profile saved!');
        } catch (err) {
            // eslint-disable-next-line no-console
            console.error('Save failed', err);
            // eslint-disable-next-line no-alert
            alert('Save failed');
        }
    };

    return (
        <>
            <Header isAuth />
            <section className="profile-page container">
                {/* Sidebar */}
                <aside className="profile-sidebar card">
                    <div className="avatar-wrapper">
                        <img src={form.avatar || '/images/avatar-profile.png'} alt="User avatar" className="avatar-lg" />
                        <span className="status-badge" />
                    </div>
                    <h3 className="user-name">{`${form.firstName} ${form.lastName}`}</h3>
                    <p className="location-text">{location.address || form.location}</p>
                    <span className="status-text active">Active</span>

                    <div className="sidebar-section">
                        <h4>Preferred Sports</h4>
                        <div className="tags">
                            {form.sports.map((s) => (
                                <span key={s} className="tag">
                                    {s}
                                </span>
                            ))}
                        </div>
                    </div>

                    <div className="sidebar-section">
                        <h3>User Information</h3>
                        {location.address && (
                            <div className="location-info">
                                <h4>Current Location</h4>
                                <p className="address">{location.address}</p>
                                <p className="coords">({location.lat?.toFixed(5)}, {location.lon?.toFixed(5)})</p>
                            </div>
                        )}
                        <h4>Skill Level</h4>
                        <p>{form.skillLevel}</p>
                    </div>

                    <div className="sidebar-section">
                        <h4>Availability</h4>
                        <div className="tags">
                            {daysOfWeek.flatMap((d) =>
                                form.availability[d].map((slot) => (
                                    <span key={`${d}-${slot}`} className="tag avail-tag">
                                        🗓️ {d.slice(0, 3)} {slot.split(' ')[0]}
                                    </span>
                                ))
                            )}
                            {daysOfWeek.every((d) => form.availability[d].length === 0) && (<span className="small-text">—</span>)}
                        </div>
                    </div>
                </aside>

                {/* Form content */}
                <div className="profile-content card" style={{position:'relative'}}>
                        <button type="button" className="refresh-btn" onClick={() => window.location.reload()} aria-label="Refresh profile">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polyline points="23 4 23 10 17 10"></polyline><polyline points="1 20 1 14 7 14"></polyline><path d="M3.51 9a9 9 0 0114.13-3.36L23 10"></path><path d="M20.49 15a9 9 0 01-14.13 3.36L1 14"></path></svg>
                        </button>
                    <h2>Edit Profile</h2>
                    <p className="subtitle">Update your information to find better matches</p>
                    <form onSubmit={handleSave} className="profile-form">
                        <div className="two-col">
                            <div className="form-group">
                                <label htmlFor="firstName">First Name</label>
                                <input
                                    id="firstName"
                                    name="firstName"
                                    value={form.firstName}
                                    onChange={handleInput}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="lastName">Last Name</label>
                                <input
                                    id="lastName"
                                    name="lastName"
                                    value={form.lastName}
                                    onChange={handleInput}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="email">Email (read-only)</label>
                            <input
                                id="email"
                                name="email"
                                value={form.email}
                                readOnly
                                disabled
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="location">Location (read-only)</label>
                            <input
                                id="location"
                                name="location"
                                value={form.location}
                                readOnly
                                disabled
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="bio">Bio</label>
                            <textarea
                                id="bio"
                                name="bio"
                                rows="3"
                                value={form.bio}
                                onChange={handleInput}
                            />
                        </div>

                        <fieldset className="form-group">
                            <legend>Preferred Sports</legend>
                            <div className="checkbox-grid">
                                {allSports.map((sport) => (
                                    <label
                                        key={sport}
                                        className={`checkbox-label ${form.sports.includes(sport) ? 'selected' : ''}`}
                                    >
                                        <input
                                            type="checkbox"
                                            checked={form.sports.includes(sport)}
                                            onChange={() => handleCheckbox('sports', sport)}
                                        />
                                        <span>{sport}</span>
                                    </label>
                                ))}

                            </div>
                        </fieldset>

                        <fieldset className="form-group">
                            <legend>Skill Level</legend>
                            <div className="radio-group">
                                {skillLevels.map((level) => (
                                    <label key={level} className={`radio-label ${form.skillLevel === level ? 'selected' : ''}`}>
                                        <input
                                            type="radio"
                                            name="skillLevel"
                                            value={level}
                                            checked={form.skillLevel === level}
                                            onChange={handleInput}
                                        />
                                        <span>{level}</span>
                                    </label>
                                ))}
                            </div>
                        </fieldset>

                        <fieldset className="form-group">
                            <legend>Availability</legend>
                            <div className="availability-list">
                                {daysOfWeek.map((day) => (
                                    <div key={day} className="day-row">
                                        <label className={`checkbox-label day-select ${form.availability[day].length > 0 ? 'selected' : ''}`}>
                                            <input
                                                type="checkbox"
                                                checked={form.availability[day].length > 0}
                                                onChange={() => toggleDay(day)}
                                            />
                                            <span>{day}</span>
                                        </label>
                                        {form.availability[day].length > 0 && (
                                            <div className="time-options">
                                                {timeSlots.map((slot) => (
                                                    <label
                                                        key={slot}
                                                        className={`checkbox-label ${form.availability[day].includes(slot) ? 'selected' : ''}`}
                                                    >
                                                        <input
                                                            type="checkbox"
                                                            checked={form.availability[day].includes(slot)}
                                                            onChange={() => toggleTime(day, slot)}
                                                        />
                                                        <span className="icon-schedule" role="img" aria-label="schedule">🗓️</span>
                                                        <span>{slot}</span>
                                                    </label>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        </fieldset>

                        <div className="form-actions">
                            <button type="button" className="btn cancel" onClick={() => setForm(initialState)}>
                                Cancel
                            </button>
                            <button type="submit" className="btn btn-primary save">
                                Save Profile
                            </button>
                        </div>
                    </form>
                </div>
            </section>
        </>
    );
}

export default ProfilePage;
