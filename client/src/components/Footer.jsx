import React from 'react';
import '../styles/LandingPage.css';

const Footer = () => (
  <footer className="footer secondary-bg" id="contact">
    <div className="container footer-grid">
      <div>
        <h3 className="brand">SportMatch</h3>
        <p className="small-text">Connecting athletes worldwide</p>
      </div>
      <div>
        <h4>Company</h4>
        <ul>
          <li><a href="#about">About</a></li>
          <li><a href="#contact">Contact</a></li>
          <li><a href="#careers">Careers</a></li>
        </ul>
      </div>
      <div>
        <h4>Legal</h4>
        <ul>
          <li><a href="#terms">Terms</a></li>
          <li><a href="#privacy">Privacy</a></li>
          <li><a href="#support">Support</a></li>
        </ul>
      </div>
      <div>
        <h4>Follow Us</h4>
        <ul className="social-links">
          <li><a href="https://instagram.com" aria-label="Instagram">📷</a></li>
          <li><a href="https://twitter.com" aria-label="Twitter">🐦</a></li>
          <li><a href="https://facebook.com" aria-label="Facebook">📘</a></li>
        </ul>
      </div>
    </div>
    <div className="container copyright">
      © {new Date().getFullYear()} SportMatch. All rights reserved.
    </div>
  </footer>
);

export default Footer;
