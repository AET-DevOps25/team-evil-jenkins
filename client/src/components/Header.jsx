import React from 'react';
import { NavLink, Link } from 'react-router-dom';
import '../styles/LandingPage.css';

const Header = ({ isAuth = true }) => {
    const authNav = [
        { path: '/home', label: 'Home' },
        { path: '/matching', label: 'Matching' },
        { path: '/profile', label: 'Profile' },
        { path: '/messages', label: 'Messages' },
        { path: '/events', label: 'Events' }
    ];

    const guestNav = [
        { href: '#how-it-works', label: 'How It Works' },
        { href: '#testimonials', label: 'Testimonials' },
        { href: '#contact', label: 'Contact' }
    ];

    return (
        <header className="header">
            <div className="container nav-container">
                <div className="brand">
                    <img src="/logo.png" alt="SportMatch logo" className="brand-logo" />
                    <span>SportMatch</span>
                </div>

                <nav>
                    {isAuth ? (
                        <ul className="nav-links">
                            {authNav.map((item) => (
                                <li key={item.path}>
                                    <NavLink
                                        to={item.path}
                                        className={({ isActive }) => (isActive ? 'active' : '')}
                                    >
                                        {item.label}
                                    </NavLink>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <ul className="nav-links">
                            {guestNav.map((item) => (
                                <li key={item.href}>
                                    <a href={item.href}>{item.label}</a>
                                </li>
                            ))}
                        </ul>
                    )}
                </nav>

                {isAuth ? (
                    <div className="auth-links">
                        <button className="icon-btn" aria-label="Notifications">
                            <img src="/images/icon-bell.svg" alt="Bell" />
                        </button>
                        <img src="/images/avatar-user.jpg" alt="User avatar" className="avatar-small" />
                    </div>
                ) : (
                    <div className="auth-links">
                        <Link to="/signin" className="signin">Sign In</Link>
                        <Link to="/signup" className="btn btn-secondary">Get Started</Link>
                    </div>
                )}
            </div>
        </header>
    );


};

export default Header;

