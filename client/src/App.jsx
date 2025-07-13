import './App.css';
import { Routes, Route } from 'react-router-dom';

import LandingPage from './pages/LandingPage';
import CallbackPage from './pages/CallbackPage';
import RequireAuth from './components/RequireAuth';
import useRegisterUser from './hooks/useRegisterUser';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import ProfilePage from './pages/ProfilePage';
import MessagesPage from './pages/MessagesPage';
import MatchingPage from './pages/MatchingPage';
import NotFoundPage from './pages/NotFoundPage';

function App() {
  // Ensure user profile exists in backend once authenticated
  useRegisterUser();
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/callback" element={<CallbackPage />} />
      <Route path="/signin/" element={<SignInPage />} />
      <Route path="/signup/" element={<SignUpPage />} />
      <Route
        path="/profile/"
        element={
          <RequireAuth>
            <ProfilePage />
          </RequireAuth>
        }
      />
      <Route
        path="/messages/"
        element={
          <RequireAuth>
            <MessagesPage />
          </RequireAuth>
        }
      />
      <Route
        path="/matches/"
        element={
          <RequireAuth>
            <MatchingPage />
          </RequireAuth>
        }
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}

export default App
