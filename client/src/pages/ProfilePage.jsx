import React, { useState } from 'react';
import Header from '../components/Header';
import '../styles/ProfilePage.css';

const initialState = {
    firstName: 'Lena',
    lastName: 'Martinez',
    location: 'San Francisco, CA',
    bio: 'New to San Francisco and looking for hiking buddies! Love exploring nature trails and staying active.',
    sports: ['Hiking', 'Running', 'Cycling'],
    skillLevel: 'Beginner',
    availability: {
        weekdays: ['Evening (6-10 PM)'],
        weekends: ['Morning (6-12 PM)'],
    },
};

const allSports = ['Hiking', 'Running', 'Cycling', 'Swimming', 'Tennis', 'Basketball'];
const skillLevels = ['Beginner', 'Intermediate', 'Advanced'];

function ProfilePage() {
    const [form, setForm] = useState(initialState);

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

    const handleAvail = (period, dayType, value) => {
        setForm((prev) => {
            const current = new Set(prev.availability[dayType]);
            current.has(value) ? current.delete(value) : current.add(value);
            return { ...prev, availability: { ...prev.availability, [dayType]: Array.from(current) } };
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
                        <img src="/images/avatar-profile.png" alt="User avatar" className="avatar-lg" />
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
                        <p>Weekends: {form.availability.weekends.join(', ') || '—'}</p>
                        <p>Weekdays: {form.availability.weekdays.join(', ') || '—'}</p>
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

                        <div className="form-group">
                            <label htmlFor="skillLevel">Skill Level</label>
                            <select
                                id="skillLevel"
                                name="skillLevel"
                                value={form.skillLevel}
                                onChange={handleInput}
                            >
                                {skillLevels.map((level) => (
                                    <option key={level}>{level}</option>
                                ))}
                            </select>
                        </div>

                        <fieldset className="form-group">
                            <legend>Availability</legend>
                            <div className="availability-grid">
                                <div>
                                    <h5>Weekdays</h5>
                                    {['Morning (6-12 PM)', 'Afternoon (12-6 PM)', 'Evening (6-10 PM)'].map((slot) => (
                                        <label
                                            key={slot}
                                            className={`checkbox-label ${form.availability.weekends.includes(slot) ? 'selected' : ''}`}
                                        >
                                            <input
                                                type="checkbox"
                                                checked={form.availability.weekends.includes(slot)}
                                                onChange={() => handleAvail('availability', 'weekends', slot)}
                                            />
                                            <span>{slot}</span>
                                        </label>
                                    ))}

                                </div>
                                <div>
                                    <h5>Weekends</h5>
                                    {['Morning (6-12 PM)', 'Afternoon (12-6 PM)', 'Evening (6-10 PM)'].map((slot) => (
                                        <label
                                            key={slot}
                                            className={`checkbox-label ${form.availability.weekends.includes(slot) ? 'selected' : ''}`}
                                        >
                                            <input
                                                type="checkbox"
                                                checked={form.availability.weekends.includes(slot)}
                                                onChange={() => handleAvail('availability', 'weekends', slot)}
                                            />
                                            <span>{slot}</span>
                                        </label>
                                    ))}

                                </div>
                            </div>
                        </fieldset>

                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary save"><img src="/images/icon-save.svg" alt="" /> Save Profile</button>
                            <button type="button" className="btn cancel" onClick={() => setForm(initialState)}>
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </section>
        </>
    );
}

export default ProfilePage;
