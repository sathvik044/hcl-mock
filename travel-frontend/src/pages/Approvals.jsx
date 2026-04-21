import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { getPendingManagerApprovals, getPendingFinanceApprovals } from '../api/approvals';
import { managerApprove, managerReject, financeApprove, financeReject } from '../api/travelRequests';
import { StatusBadge, Modal, EmptyState, formatCurrency, formatDate } from '../components/UI';

export default function Approvals() {
  const { user, isRole } = useAuth();
  const [tab,      setTab]      = useState(isRole('FINANCE') && !isRole('ADMIN','MANAGER') ? 'finance' : 'manager');
  const [pending,  setPending]  = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [actionModal, setActionModal] = useState(null); // { req, type }
  const [remarks,  setRemarks]  = useState('');
  const [saving,   setSaving]   = useState(false);

  useEffect(() => { fetchPending(); }, [tab]);

  const fetchPending = async () => {
    setLoading(true);
    try {
      if (tab === 'manager') {
        const res = await getPendingManagerApprovals();
        setPending(res.data ?? []);
      } else {
        const res = await getPendingFinanceApprovals();
        setPending(res.data ?? []);
      }
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const handleAction = async () => {
    if (!actionModal) return;
    setSaving(true);
    try {
      const { req, type } = actionModal;
      if (type === 'manager-approve') await managerApprove(req.id);
      else if (type === 'manager-reject') await managerReject(req.id);
      else if (type === 'finance-approve') await financeApprove(req.id);
      else if (type === 'finance-reject')  await financeReject(req.id);
      setActionModal(null);
      setRemarks('');
      fetchPending();
    } catch (err) {
      alert(err.response?.data?.message ?? 'Action failed');
    } finally { setSaving(false); }
  };

  const isApprove = actionModal?.type.includes('approve');

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Approvals</h2>
          <p className="page-subtitle">{pending.length} pending approval{pending.length !== 1 ? 's' : ''}</p>
        </div>
      </div>

      {/* Tabs */}
      {isRole('ADMIN','MANAGER') && isRole('ADMIN','FINANCE') && (
        <div className="tabs mb-lg">
          <button className={`tab ${tab === 'manager' ? 'active' : ''}`}  onClick={() => setTab('manager')}>Manager Queue</button>
          <button className={`tab ${tab === 'finance' ? 'active' : ''}`}  onClick={() => setTab('finance')}>Finance Queue</button>
        </div>
      )}

      <div className="glass-card">
        {loading ? (
          <div className="loading-screen"><div className="spinner spinner-lg" /></div>
        ) : pending.length === 0 ? (
          <EmptyState icon="✅" title="All caught up!" desc="No pending approvals at this time." />
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Request #</th>
                  <th>Employee</th>
                  <th>Route</th>
                  <th>Type</th>
                  <th>Est. Cost</th>
                  <th>Travel Dates</th>
                  <th>Purpose</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pending.map(req => (
                  <tr key={req.id}>
                    <td style={{ fontWeight: 700 }}>{req.requestNumber}</td>
                    <td>{req.employeeName}</td>
                    <td style={{ color: 'var(--color-text-primary)' }}>{req.fromCity} → {req.toCity}</td>
                    <td>
                      <span className={`badge ${req.travelType === 'INTERNATIONAL' ? 'badge-booked' : 'badge-submitted'}`}>
                        {req.travelType}
                      </span>
                    </td>
                    <td style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>{formatCurrency(req.estimatedCost)}</td>
                    <td>{formatDate(req.fromDate)} – {formatDate(req.toDate)}</td>
                    <td className="text-sm">{req.purpose?.replace(/_/g, ' ')}</td>
                    <td>
                      <div className="flex gap-sm">
                        <button className="btn btn-success btn-sm"
                          onClick={() => setActionModal({ req, type: `${tab}-approve` })}>
                          ✅ Approve
                        </button>
                        <button className="btn btn-danger btn-sm"
                          onClick={() => setActionModal({ req, type: `${tab}-reject` })}>
                          ✕ Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Action Modal */}
      <Modal
        open={!!actionModal}
        onClose={() => { setActionModal(null); setRemarks(''); }}
        title={isApprove ? '✅ Approve Request' : '✕ Reject Request'}
        footer={<>
          <button className="btn btn-ghost" onClick={() => { setActionModal(null); setRemarks(''); }}>Cancel</button>
          <button
            className={`btn ${isApprove ? 'btn-success' : 'btn-danger'}`}
            onClick={handleAction}
            disabled={saving}
          >
            {saving ? 'Processing...' : isApprove ? 'Confirm Approval' : 'Confirm Rejection'}
          </button>
        </>}
      >
        {actionModal && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            <div className={`alert ${isApprove ? 'alert-success' : 'alert-error'}`}>
              {isApprove ? '✅' : '⚠️'} You are about to {isApprove ? 'approve' : 'reject'} request{' '}
              <strong>{actionModal.req.requestNumber}</strong> for{' '}
              <strong>{actionModal.req.employeeName}</strong> ({formatCurrency(actionModal.req.estimatedCost)}).
            </div>
            <div className="form-group">
              <label className="form-label">Remarks {!isApprove && <span style={{ color: 'var(--color-error)' }}>*</span>}</label>
              <textarea
                className="form-textarea"
                placeholder={isApprove ? 'Optional remarks...' : 'Reason for rejection (required)'}
                value={remarks}
                onChange={e => setRemarks(e.target.value)}
                required={!isApprove}
              />
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}
