import React from 'react';
import { useLocation } from 'react-router-dom';
import './Topbar.css';

const PAGE_TITLES = {
  '/dashboard':       { title: 'Dashboard',        icon: '📊' },
  '/travel-requests': { title: 'Travel Requests',  icon: '✈️' },
  '/approvals':       { title: 'Approvals',         icon: '✅' },
  '/itinerary':       { title: 'Itinerary',         icon: '🗺️' },
  '/expenses':        { title: 'Expenses',           icon: '💰' },
  '/budgets':         { title: 'Budget Management', icon: '📈' },
  '/users':           { title: 'User Management',   icon: '👥' },
};

export default function Topbar() {
  const { pathname } = useLocation();
  const page = PAGE_TITLES[pathname] || { title: 'TravelPro', icon: '✈️' };

  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="topbar-page-icon">{page.icon}</span>
        <h1 className="topbar-title">{page.title}</h1>
      </div>
      <div className="topbar-right">
        <div className="topbar-badge">
          <span className="topbar-badge-dot" />
          <span>System Online</span>
        </div>
      </div>
    </header>
  );
}
