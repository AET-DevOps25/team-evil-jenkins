import React from 'react';
import Header from '../components/Header';
import { Link } from 'react-router-dom';
import '../styles/AuthPage.css';

const SignInPage = () => {
    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO: integrate with backend auth service
    };

    return (
        <>
            <Header />
            <section className="auth-page">
                <div className="auth-illustration gradient-bg">
                    <div className="illustration-content">
                        <h2>Welcome Back</h2>
                        <p>Sign in to access your dashboard</p>
                    </div>
                </div>
                <div className="auth-form-wrapper">
                    <div className="form-box">
                        <div className="auth-avatar">
                            <img src="/images/avatar-signin.png" alt="Sign in icon" />
                        </div>
                        <h2 className="form-title">Sign In</h2>
                        <p className="form-subtitle">Enter your credentials to continue</p>
                        <form onSubmit={handleSubmit} className="auth-form">
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
                                placeholder="Enter your password"
                                required
                            />

                            <div className="form-extras">
                                <label className="remember-me">
                                    <input type="checkbox" /> Remember me
                                </label>
                                <Link to="#" className="forgot">Forgot password?</Link>
                            </div>

                            <button type="submit" className="btn btn-secondary btn-block">Sign In</button>
                        </form>
                        <p className="redirect-text">
                            Don't have an account? <Link to="/signup">Sign up</Link>
                        </p>
                    </div>
                </div>
            </section>
        </>
    );
};

export default SignInPage;
