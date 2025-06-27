import React from 'react';
import '../styles/LandingPage.css';

const steps = [
  {
    id: 1,
    title: 'Create Profile',
    description: 'Set up your profile with sports preferences, availability, and skill level.'
  },
  {
    id: 2,
    title: 'Set Preferences',
    description: 'Define your ideal partner criteria and activity preferences.'
  },
  {
    id: 3,
    title: 'Match & Connect',
    description: 'Get matched with compatible partners and start your adventure.'
  }
];

const HowItWorks = () => {
  return (
    <section id="how-it-works" className="how-it-works">
      <div className="container">
        <h2>How It Works</h2>
        <p className="subtitle">Get matched with your ideal sports partner in 3 simple steps</p>
        <div className="steps-grid">
          {steps.map(step => (
            <div key={step.id} className="step">
              <div className={`step-number step-${step.id}`}>{step.id}</div>
              <h3>{step.title}</h3>
              <p>{step.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default HowItWorks;
