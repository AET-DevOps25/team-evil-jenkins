import React from 'react';
import Header from '../components/Header';
import HeroSection from '../components/HeroSection';
import HowItWorks from '../components/HowItWorks';
import Testimonials from '../components/Testimonials';
import ContactSection from '../components/ContactSection';
import CTASection from '../components/CTASection';
import Footer from '../components/Footer';

const LandingPage = () => {
  return (
    <>
      <Header isAuth={false} />
      <main>
        <HeroSection />
        <HowItWorks />
        <Testimonials />
        <CTASection />
        <ContactSection />
      </main>
      <Footer />
    </>
  );
};

export default LandingPage;
