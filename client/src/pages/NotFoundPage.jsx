import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/Header';
import '../styles/AuthPage.css';

const NotFoundPage = () => {
  return (
    <>
      <Header isAuth={false} />
      <section className="auth-page" style={{ textAlign: 'center', padding: '4rem 1rem' }}>
        <div className="form-box">
          <h2 className="form-title">404 – Page Not Found</h2>
          <p className="form-subtitle">Oops! The page you're looking for doesn't exist.</p>
          <Link to="/" className="btn btn-secondary">Back to Home</Link>
        </div>
      </section>
    </>
  );
};

export default NotFoundPage;
