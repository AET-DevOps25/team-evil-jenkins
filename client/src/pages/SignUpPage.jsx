import React from 'react';
import Header from '../components/Header';
import { Link } from 'react-router-dom';
import '../styles/AuthPage.css';

const SignUpPage = () => {
    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO: integrate with backend registration service
    };

    return (
        <>
            <Header />
            <section className="auth-page">
                <div className="auth-illustration gradient-bg">
                    <div className="illustration-content">
                        <h2>Join SportMatch</h2>
                        <p>Create an account to find your perfect sports partner</p>
                    </div>
                </div>
                <div className="auth-form-wrapper">
                    <div className="form-box">
                        <h2 className="form-title">Sign Up</h2>
                        <p className="form-subtitle">It only takes a minute</p>
                        <form onSubmit={handleSubmit} className="auth-form">
                            <label htmlFor="name">Name</label>
                            <input id="name" className="with-icon name" name="name" placeholder="Enter your name" required />

                            <label htmlFor="email">Email Address</label>
                            <input
                                id="email" className="with-icon email"
                                type="email"
                                name="email"
                                placeholder="Enter your email"
                                required
                            />

                            <label htmlFor="password">Password</label>
                            <input
                                id="password" className="with-icon password"
                                type="password"
                                name="password"
                                placeholder="Create a password"
                                required
                            />

                            <button type="submit" className="btn btn-secondary btn-block">Create Account</button>
                        </form>
                        <p className="redirect-text">
                            Already have an account? <Link to="/signin">Sign in</Link>
                        </p>
                    </div>
                </div>
            </section>
        </>
    );
};

export default SignUpPage;
