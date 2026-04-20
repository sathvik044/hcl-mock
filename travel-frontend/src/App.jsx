import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Sidebar from './components/Sidebar';
import Topbar from './components/Topbar';

import Login          from './pages/Login';
import Dashboard      from './pages/Dashboard';
import TravelRequests from './pages/TravelRequests';
import Approvals      from './pages/Approvals';
import Itinerary      from './pages/Itinerary';
import Expenses       from './pages/Expenses';
import Budgets        from './pages/Budgets';
import Users          from './pages/Users';

function AppLayout({ children }) {
  return (
    <div className="app-layout">
      <Sidebar />
      <div className="main-content">
        <Topbar />
        <main className="page-content">
          {children}
        </main>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        {/* Public */}
        <Route path="/login" element={<Login />} />

        {/* Protected — all roles */}
        <Route path="/dashboard" element={
          <ProtectedRoute>
            <AppLayout><Dashboard /></AppLayout>
          </ProtectedRoute>
        } />

        <Route path="/travel-requests" element={
          <ProtectedRoute>
            <AppLayout><TravelRequests /></AppLayout>
          </ProtectedRoute>
        } />

        <Route path="/expenses" element={
          <ProtectedRoute>
            <AppLayout><Expenses /></AppLayout>
          </ProtectedRoute>
        } />

        {/* Manager / Finance / Admin */}
        <Route path="/approvals" element={
          <ProtectedRoute roles={['ADMIN','MANAGER','FINANCE']}>
            <AppLayout><Approvals /></AppLayout>
          </ProtectedRoute>
        } />

        {/* Travel Desk / Admin / Manager / Finance */}
        <Route path="/itinerary" element={
          <ProtectedRoute roles={['ADMIN','TRAVEL_DESK','MANAGER','FINANCE']}>
            <AppLayout><Itinerary /></AppLayout>
          </ProtectedRoute>
        } />

        {/* Admin / Finance / Manager */}
        <Route path="/budgets" element={
          <ProtectedRoute roles={['ADMIN','FINANCE','MANAGER']}>
            <AppLayout><Budgets /></AppLayout>
          </ProtectedRoute>
        } />

        {/* Admin only */}
        <Route path="/users" element={
          <ProtectedRoute roles={['ADMIN']}>
            <AppLayout><Users /></AppLayout>
          </ProtectedRoute>
        } />

        {/* Default redirects */}
        <Route path="/"  element={<Navigate to="/dashboard" replace />} />
        <Route path="*"  element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </AuthProvider>
  );
}
