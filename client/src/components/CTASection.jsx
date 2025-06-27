import React from 'react';
import '../styles/LandingPage.css';

const CTASection = () => (
  <section className="cta-section primary-bg">
    <div className="container cta-content">
      <h2>Ready to Find Your Sports Partner?</h2>
      <p>Join thousands of athletes who\'ve found their perfect workout buddy</p>
      <a href="/signup" className="btn btn-primary btn-lg">Start Matching Today</a>
    </div>
  </section>
);

export default CTASection;
