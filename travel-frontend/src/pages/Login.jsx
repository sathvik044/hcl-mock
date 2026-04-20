import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const DEMO_CREDENTIALS = [
  { role: 'Admin',        email: 'admin@corporate.com',        color: 'amber' },
  { role: 'Manager',      email: 'rajesh.kumar@corporate.com', color: 'purple' },
  { role: 'Employee',     email: 'arjun.mehta@corporate.com',  color: 'blue' },
  { role: 'Finance',      email: 'vikram.singh@corporate.com', color: 'green' },
  { role: 'Travel Desk',  email: 'meera.nair@corporate.com',   color: 'pink' },
];

export default function Login() {
  const { login }   = useAuth();
  const navigate    = useNavigate();
  const [email, setEmail]       = useState('');
  const [password, setPassword] = useState('');
  const [error, setError]       = useState('');
  const [loading, setLoading]   = useState(false);
  const [showPass, setShowPass] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const fillDemo = (demoEmail) => {
    setEmail(demoEmail);
    setPassword('Password@123');
    setError('');
  };

  return (
    <div className="login-page">
      {/* Animated background */}
      <div className="login-bg">
        <div className="orb orb-1" />
        <div className="orb orb-2" />
        <div className="orb orb-3" />
        <div className="grid-overlay" />
      </div>

      <div className="login-container">
        {/* Left panel — branding */}
        <div className="login-left">
          <div className="brand-logo animate-fade-in">
            <div className="brand-icon">✈</div>
            <div>
              <h1 className="brand-name">TravelPro</h1>
              <p className="brand-tagline">Enterprise Travel Management</p>
            </div>
          </div>

          <div className="login-hero animate-slide-up">
            <h2 className="hero-title">Streamline Your <span className="gradient-text">Corporate Travel</span></h2>
            <p className="hero-desc">
              Manage travel requests, multi-level approvals, itinerary booking,
              expense tracking and budget management — all in one platform.
            </p>

            <div className="feature-pills">
              {['🗺️ Smart Itineraries', '✅ Multi-level Approvals', '💰 Budget Tracking', '📊 Expense Reports'].map(f => (
                <span key={f} className="feature-pill">{f}</span>
              ))}
            </div>
          </div>

          {/* Demo credentials */}
          <div className="demo-credentials animate-fade-in">
            <p className="demo-title">Quick Login — Demo Accounts</p>
            <div className="demo-grid">
              {DEMO_CREDENTIALS.map(cred => (
                <button
                  key={cred.email}
                  className={`demo-chip demo-chip-${cred.color}`}
                  onClick={() => fillDemo(cred.email)}
                >
                  <span className="demo-role">{cred.role}</span>
                  <span className="demo-email">{cred.email}</span>
                </button>
              ))}
            </div>
            <p className="demo-password">All use password: <code>Password@123</code></p>
          </div>
        </div>

        {/* Right panel — login form */}
        <div className="login-right">
          <div className="login-card glass-card animate-slide-up">
            <div className="login-card-header">
              <div className="login-card-icon">🔐</div>
              <h2 className="login-card-title">Welcome Back</h2>
              <p className="login-card-subtitle">Sign in to your corporate travel account</p>
            </div>

            <form onSubmit={handleSubmit} className="login-form">
              {error && (
                <div className="alert alert-error animate-fade-in">
                  <span>⚠️</span> {error}
                </div>
              )}

              <div className="form-group">
                <label className="form-label">Work Email</label>
                <div className="input-wrapper">
                  <span className="input-icon">📧</span>
                  <input
                    id="login-email"
                    type="email"
                    className="form-input input-with-icon"
                    placeholder="you@corporate.com"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                    autoFocus
                  />
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Password</label>
                <div className="input-wrapper">
                  <span className="input-icon">🔒</span>
                  <input
                    id="login-password"
                    type={showPass ? 'text' : 'password'}
                    className="form-input input-with-icon"
                    placeholder="Enter your password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                  />
                  <button
                    type="button"
                    className="toggle-password"
                    onClick={() => setShowPass(p => !p)}
                  >
                    {showPass ? '🙈' : '👁️'}
                  </button>
                </div>
              </div>

              <button
                id="login-submit"
                type="submit"
                className="btn btn-primary btn-lg w-full login-btn"
                disabled={loading}
              >
                {loading ? (
                  <><span className="spinner" style={{ width: 18, height: 18 }} /> Authenticating...</>
                ) : (
                  <><span>Sign In</span><span>→</span></>
                )}
              </button>
            </form>

            <div className="login-footer">
              <div className="security-badge">
                <span>🔰</span>
                <span>Secured with JWT Authentication · 256-bit encryption</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
