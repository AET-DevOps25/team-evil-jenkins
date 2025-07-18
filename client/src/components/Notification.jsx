import React from 'react';
import PropTypes from 'prop-types';
import '../styles/Notification.css';

const icons = {
  success: '✔️',
  error: '❌',
  warning: '⚠️',
  info: 'ℹ️',
};

const Notification = ({ type = 'info', message, onClose }) => (
  <div className={`notification toast-${type}`} role="alert" aria-live="assertive">
    <span className="icon" aria-hidden="true">{icons[type]}</span>
    <span className="message">{message}</span>
    <button className="close-btn" onClick={onClose} aria-label="Dismiss notification">×</button>
  </div>
);

Notification.propTypes = {
  type: PropTypes.oneOf(['success', 'error', 'warning', 'info']),
  message: PropTypes.string.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default Notification;
