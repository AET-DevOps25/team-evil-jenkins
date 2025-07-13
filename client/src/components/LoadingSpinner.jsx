import React from 'react';

export default function LoadingSpinner({ text = 'Loading…' }) {
  return (
    <main style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
      <p>{text}</p>
    </main>
  );
}
