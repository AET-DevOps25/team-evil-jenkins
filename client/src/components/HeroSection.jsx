import React from 'react';
import '../styles/LandingPage.css';

const HeroSection = () => {
  return (
    <section className="hero-section gradient-bg">
      <div className="container hero-content">
        <div className="hero-text">
          <h1>Find Your Perfect Sports Partner</h1>
          <p>
            Connect with like-minded athletes in your area. From hiking to cycling,
            find your next adventure buddy.
          </p>
          <div className="hero-buttons">
            <a href="/signup" className="btn btn-primary">Get Started</a>
            <a href="#how-it-works" className="btn btn-outline">See How It Works</a>
          </div>
        </div>
        <div className="hero-image">
          {/* Placeholder image ‑ replace src when real image provided */}
          <img
            src="/images/hero-placeholder.jpg"
            alt="Group of outdoor athletes"
          />
        </div>
      </div>
    </section>
  );
};

export default HeroSection;
