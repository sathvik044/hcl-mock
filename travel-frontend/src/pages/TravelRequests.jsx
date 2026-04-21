import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import {
  getTravelRequests, getMyRequests, createTravelRequest, submitRequest, cancelRequest
} from '../api/travelRequests';
import { StatusBadge, Modal, EmptyState, formatCurrency, formatDate } from '../components/UI';

const TRAVEL_TYPES   = ['DOMESTIC', 'INTERNATIONAL'];
const PURPOSES       = ['CLIENT_MEETING', 'CONFERENCE', 'TRAINING', 'INTERNAL_MEETING'];

export default function TravelRequests() {
  const { user, isRole } = useAuth();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [page,   setPage]       = useState(0);
  const [total,  setTotal]      = useState(0);

  const [showCreate, setShowCreate] = useState(false);
  const [showDetail, setShowDetail] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    employeeId: user?.userId, travelType: 'DOMESTIC', purpose: 'CLIENT_MEETING',
    fromDate: '', toDate: '', fromCity: '', toCity: '', estimatedCost: ''
  });

  useEffect(() => { fetchRequests(); }, [page]);

  const fetchRequests = async () => {
    setLoading(true);
    try {
      let res;
      if (isRole('EMPLOYEE')) {
        res = await getMyRequests(user.userId, { page, size: 10 });
      } else {
        res = await getTravelRequests({ page, size: 10 });
      }
      const data = Array.isArray(res.data) ? res.data : [];
      setRequests(data);
      setTotal(data.length);
    } catch (e) { console.error(e); }
    finally   { setLoading(false); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await createTravelRequest({ ...form, estimatedCost: parseFloat(form.estimatedCost) });
      setShowCreate(false);
      setForm({ employeeId: user?.userId, travelType: 'DOMESTIC', purpose: 'CLIENT_MEETING', fromDate: '', toDate: '', fromCity: '', toCity: '', estimatedCost: '' });
      fetchRequests();
    } catch (err) {
      alert(err.response?.data?.message ?? 'Failed to create request');
    } finally { setSubmitting(false); }
  };

  const handleSubmit = async (id) => {
    try {
      await submitRequest(id);
      fetchRequests();
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to submit'); }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('Cancel this travel request?')) return;
    try {
      await cancelRequest(id);
      fetchRequests();
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to cancel'); }
  };

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Travel Requests</h2>
          <p className="page-subtitle">{total} total request{total !== 1 ? 's' : ''}</p>
        </div>
        {isRole('EMPLOYEE','ADMIN') && (
          <button className="btn btn-primary" onClick={() => setShowCreate(true)}>
            + New Request
          </button>
        )}
      </div>

      <div className="glass-card">
        {loading ? (
          <div className="loading-screen"><div className="spinner spinner-lg" /></div>
        ) : requests.length === 0 ? (
          <EmptyState icon="✈️" title="No travel requests" desc="Create your first travel request to get started." />
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Request #</th>
                  <th>Employee</th>
                  <th>Route</th>
                  <th>Dates</th>
                  <th>Type</th>
                  <th>Est. Cost</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {requests.map(req => (
                  <tr key={req.id}>
                    <td>
                      <button className="btn btn-ghost btn-sm" onClick={() => setShowDetail(req)}>
                        {req.requestNumber}
                      </button>
                    </td>
                    <td>{req.employeeName}</td>
                    <td style={{ color: 'var(--color-text-primary)' }}>{req.fromCity} → {req.toCity}</td>
                    <td>{formatDate(req.fromDate)} – {formatDate(req.toDate)}</td>
                    <td>
                      <span className={`badge ${req.travelType === 'INTERNATIONAL' ? 'badge-booked' : 'badge-submitted'}`}>
                        {req.travelType}
                      </span>
                    </td>
                    <td style={{ color: 'var(--color-text-primary)' }}>{formatCurrency(req.estimatedCost)}</td>
                    <td><StatusBadge status={req.status} /></td>
                    <td>
                      <div className="flex gap-sm">
                        {req.status === 'DRAFT' && isRole('EMPLOYEE','ADMIN') && (
                          <button className="btn btn-success btn-sm" onClick={() => handleSubmit(req.id)}>Submit</button>
                        )}
                        {['DRAFT','SUBMITTED','MANAGER_APPROVED'].includes(req.status) && (
                          <button className="btn btn-danger btn-sm" onClick={() => handleCancel(req.id)}>Cancel</button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination */}
        {total > 10 && (
          <div className="flex items-center justify-between" style={{ padding: '12px 16px', borderTop: '1px solid var(--color-border)' }}>
            <span className="text-sm text-muted">Page {page + 1} of {Math.ceil(total / 10)}</span>
            <div className="flex gap-sm">
              <button className="btn btn-ghost btn-sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Prev</button>
              <button className="btn btn-ghost btn-sm" disabled={(page + 1) * 10 >= total} onClick={() => setPage(p => p + 1)}>Next →</button>
            </div>
          </div>
        )}
      </div>

      {/* Create Modal */}
      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="✈️ New Travel Request"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setShowCreate(false)}>Cancel</button>
          <button className="btn btn-primary" form="create-form" type="submit" disabled={submitting}>
            {submitting ? 'Creating...' : 'Create Request'}
          </button>
        </>}
      >
        <form id="create-form" onSubmit={handleCreate} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Travel Type</label>
              <select className="form-select" value={form.travelType} onChange={e => setForm(f => ({ ...f, travelType: e.target.value }))}>
                {TRAVEL_TYPES.map(t => <option key={t}>{t}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Purpose</label>
              <select className="form-select" value={form.purpose} onChange={e => setForm(f => ({ ...f, purpose: e.target.value }))}>
                {PURPOSES.map(p => <option key={p}>{p}</option>)}
              </select>
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">From City</label>
              <input className="form-input" required value={form.fromCity} onChange={e => setForm(f => ({ ...f, fromCity: e.target.value }))} placeholder="e.g. Bengaluru" />
            </div>
            <div className="form-group">
              <label className="form-label">To City</label>
              <input className="form-input" required value={form.toCity} onChange={e => setForm(f => ({ ...f, toCity: e.target.value }))} placeholder="e.g. Mumbai" />
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">From Date</label>
              <input className="form-input" type="date" required value={form.fromDate} onChange={e => setForm(f => ({ ...f, fromDate: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">To Date</label>
              <input className="form-input" type="date" required value={form.toDate} onChange={e => setForm(f => ({ ...f, toDate: e.target.value }))} />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Estimated Cost (₹)</label>
            <input className="form-input" type="number" required min="0" step="100" value={form.estimatedCost} onChange={e => setForm(f => ({ ...f, estimatedCost: e.target.value }))} placeholder="25000" />
          </div>
        </form>
      </Modal>

      {/* Detail Modal */}
      <Modal open={!!showDetail} onClose={() => setShowDetail(null)} title={`Request ${showDetail?.requestNumber}`} size="modal-lg"
        footer={<button className="btn btn-ghost" onClick={() => setShowDetail(null)}>Close</button>}
      >
        {showDetail && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
            <div className="grid-2">
              {[
                ['Employee', showDetail.employeeName],
                ['Status',   <StatusBadge status={showDetail.status} key="s" />],
                ['From',     `${showDetail.fromCity} → ${showDetail.toCity}`],
                ['Type',     showDetail.travelType],
                ['Dates',    `${formatDate(showDetail.fromDate)} – ${formatDate(showDetail.toDate)}`],
                ['Est. Cost', formatCurrency(showDetail.estimatedCost)],
              ].map(([k, v]) => (
                <div key={k}>
                  <div className="text-xs text-muted mb-sm">{k}</div>
                  <div style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>{v}</div>
                </div>
              ))}
            </div>

            {false && showDetail.approvals?.length > 0 && (
              <div>
                <div className="section-divider" />
                <h4 style={{ fontWeight: 700, marginBottom: 12, fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>APPROVALS</h4>
                {showDetail.approvals.map(a => (
                  <div key={a.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)' }}>
                    <span className="text-sm">{a.approverType} — {a.approver?.name}</span>
                    <StatusBadge status={a.status} />
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
}
