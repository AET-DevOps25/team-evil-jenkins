import './App.css';
import { Routes, Route } from 'react-router-dom';

import LandingPage from './pages/LandingPage';
import CallbackPage from './pages/CallbackPage.jsx';
import LogoutPage from './pages/LogoutPage.jsx';
import useUpdateLocation from './hooks/useUpdateLocation.js';
import RequireAuth from './components/RequireAuth';
import useRegisterUser from './hooks/useRegisterUser';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import ProfilePage from './pages/ProfilePage';
import MessagesPage from './pages/MessagesPage';
import MatchingPage from './pages/MatchingPage';
import NotFoundPage from './pages/NotFoundPage';
import Home from './pages/LandingPage';

function App() {
  // Ensure user profile exists in backend once authenticated
  useRegisterUser();
  useUpdateLocation();
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/callback" element={<CallbackPage />} />
      <Route path="/logout" element={<LogoutPage />} />
      <Route path="/home" element={<Home />} />
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
