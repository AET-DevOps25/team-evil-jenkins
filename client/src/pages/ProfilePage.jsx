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

function ProfilePage() {
    const { user } = useAuth0();
    const [form, setForm] = useState(initialState);

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



    const handleSave = (e) => {
        e.preventDefault();
        // TODO: connect to backend API
        alert('Profile saved (stub)');
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
                    <p className="location-text">{form.location}</p>
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
                <div className="profile-content card">
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
                            <label htmlFor="location">Location</label>
                            <input
                                id="location"
                                name="location"
                                value={form.location}
                                onChange={handleInput}
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
