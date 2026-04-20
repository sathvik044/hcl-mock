import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Sidebar.css';

const NAV_ITEMS = [
  { to: '/dashboard',       icon: '📊', label: 'Dashboard',       roles: ['ADMIN','MANAGER','EMPLOYEE','FINANCE','TRAVEL_DESK'] },
  { to: '/travel-requests', icon: '✈️',  label: 'Travel Requests', roles: ['ADMIN','MANAGER','EMPLOYEE','FINANCE','TRAVEL_DESK'] },
  { to: '/approvals',       icon: '✅', label: 'Approvals',       roles: ['ADMIN','MANAGER','FINANCE'] },
  { to: '/itinerary',       icon: '🗺️',  label: 'Itinerary',      roles: ['ADMIN','TRAVEL_DESK','MANAGER','FINANCE'] },
  { to: '/expenses',        icon: '💰', label: 'Expenses',        roles: ['ADMIN','EMPLOYEE','MANAGER','FINANCE'] },
  { to: '/budgets',         icon: '📈', label: 'Budgets',         roles: ['ADMIN','FINANCE','MANAGER'] },
  { to: '/users',           icon: '👥', label: 'Users',           roles: ['ADMIN'] },
];

export default function Sidebar() {
  const { user, logout, isRole } = useAuth();
  const navigate = useNavigate();

  const visibleItems = NAV_ITEMS.filter(item => isRole(...item.roles));

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getRoleBadgeClass = (role) => `badge badge-role-${role?.toLowerCase()}`;

  return (
    <aside className="sidebar">
      {/* Logo */}
      <div className="sidebar-logo">
        <div className="sidebar-brand-icon">✈</div>
        <div>
          <span className="sidebar-brand-name">TravelPro</span>
          <span className="sidebar-brand-sub">Corporate</span>
        </div>
      </div>

      {/* User info */}
      <div className="sidebar-user">
        <div className="sidebar-avatar">{user?.name?.charAt(0)?.toUpperCase()}</div>
        <div className="sidebar-user-info">
          <span className="sidebar-user-name">{user?.name}</span>
          <span className={getRoleBadgeClass(user?.role)}>{user?.role}</span>
        </div>
      </div>

      <div className="sidebar-divider" />

      {/* Navigation */}
      <nav className="sidebar-nav">
        <p className="sidebar-nav-label">Navigation</p>
        {visibleItems.map(item => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `sidebar-nav-item ${isActive ? 'active' : ''}`
            }
          >
            <span className="nav-icon">{item.icon}</span>
            <span className="nav-label">{item.label}</span>
          </NavLink>
        ))}
      </nav>

      {/* Bottom */}
      <div className="sidebar-bottom">
        <div className="sidebar-dept">
          <span className="dept-icon">🏢</span>
          <span className="dept-text">{user?.department}</span>
        </div>
        <button className="sidebar-logout" onClick={handleLogout}>
          <span>🚪</span>
          <span>Sign Out</span>
        </button>
      </div>
    </aside>
  );
}
