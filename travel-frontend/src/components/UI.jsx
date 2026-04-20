import React from 'react';

export function StatusBadge({ status }) {
  const map = {
    DRAFT:             'draft',
    SUBMITTED:         'submitted',
    MANAGER_APPROVED:  'approved',
    MANAGER_REJECTED:  'rejected',
    FINANCE_APPROVED:  'approved',
    FINANCE_REJECTED:  'rejected',
    BOOKED:            'booked',
    COMPLETED:         'completed',
    CANCELLED:         'cancelled',
    PENDING:           'pending',
    VERIFIED:          'verified',
    REIMBURSED:        'reimbursed',
    APPROVED:          'approved',
    REJECTED:          'rejected',
    CONFIRMED:         'approved',
  };
  const cls = map[status] || 'draft';
  return <span className={`badge badge-${cls}`}>{status?.replace(/_/g, ' ')}</span>;
}

export function RoleBadge({ role }) {
  return <span className={`badge badge-role-${role?.toLowerCase()}`}>{role}</span>;
}

export function LoadingSpinner({ size = '' }) {
  return (
    <div className="loading-screen">
      <div className={`spinner ${size === 'lg' ? 'spinner-lg' : ''}`} />
      <span>Loading...</span>
    </div>
  );
}

export function EmptyState({ icon = '📭', title = 'No data found', desc = '' }) {
  return (
    <div className="empty-state">
      <div className="empty-state-icon">{icon}</div>
      <div className="empty-state-title">{title}</div>
      {desc && <p className="empty-state-desc">{desc}</p>}
    </div>
  );
}

export function Modal({ open, onClose, title, children, footer, size = '' }) {
  if (!open) return null;
  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className={`modal ${size}`} onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <span className="modal-title">{title}</span>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body">{children}</div>
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  );
}

export function StatCard({ icon, label, value, color = 'blue', subtitle }) {
  return (
    <div className={`stat-card ${color}`}>
      <div className={`stat-icon ${color}`}>{icon}</div>
      <div className="stat-value">{value ?? '—'}</div>
      <div className="stat-label">{label}</div>
      {subtitle && <div className="text-xs text-muted mt-sm">{subtitle}</div>}
    </div>
  );
}

export function formatCurrency(amount) {
  if (amount == null) return '—';
  return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(amount);
}

export function formatDate(dateStr) {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}
