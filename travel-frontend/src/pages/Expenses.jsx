import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { getTravelRequests, getMyRequests } from '../api/travelRequests';
import { submitExpense, getExpensesByReq, verifyExpense, reimburseExpense, rejectExpense } from '../api/expenses';
import { StatusBadge, Modal, EmptyState, formatCurrency, formatDate } from '../components/UI';

const EXPENSE_TYPES = ['FLIGHT','HOTEL','MEAL','LOCAL_TRANSPORT','VISA','MISCELLANEOUS'];

export default function Expenses() {
  const { user, isRole } = useAuth();
  const [requests,  setRequests]  = useState([]);
  const [selected,  setSelected]  = useState(null);
  const [expenses,  setExpenses]  = useState([]);
  const [loading,   setLoading]   = useState(false);
  const [showAdd,   setShowAdd]   = useState(false);
  const [saving,    setSaving]    = useState(false);
  const [verifyModal, setVerifyModal] = useState(null);
  const [notes,     setNotes]     = useState('');
  const [form, setForm] = useState({
    expenseType: 'MEAL', amount: '', receiptUrl: '', notes: '', travelRequestId: ''
  });

  useEffect(() => { fetchRequests(); }, []);
  useEffect(() => { if (selected) fetchExpenses(selected.id); }, [selected]);

  const fetchRequests = async () => {
    try {
      const res = isRole('EMPLOYEE')
        ? await getMyRequests(user.userId, { size: 100 })
        : await getTravelRequests({ size: 100 });
      const list = res.data.data?.content ?? [];
      const filtered = list.filter(r => ['BOOKED','COMPLETED'].includes(r.status));
      setRequests(filtered);
      if (filtered.length > 0) setSelected(filtered[0]);
    } catch (e) { console.error(e); }
  };

  const fetchExpenses = async (reqId) => {
    setLoading(true);
    try {
      const res = await getExpensesByReq(reqId);
      setExpenses(res.data.data ?? []);
    } catch (e) { setExpenses([]); }
    finally { setLoading(false); }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await submitExpense({ ...form, travelRequestId: selected.id, claimedById: user.userId, amount: parseFloat(form.amount) });
      setShowAdd(false);
      setForm({ expenseType: 'MEAL', amount: '', receiptUrl: '', notes: '', travelRequestId: '' });
      fetchExpenses(selected.id);
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to submit expense'); }
    finally { setSaving(false); }
  };

  const handleVerify = async () => {
    setSaving(true);
    try {
      await verifyExpense(verifyModal.id, { verifiedById: user.userId, notes });
      setVerifyModal(null); setNotes('');
      fetchExpenses(selected.id);
    } catch (err) { alert(err.response?.data?.message ?? 'Failed'); }
    finally { setSaving(false); }
  };

  const handleReimburse = async (id) => {
    try { await reimburseExpense(id, { processedById: user.userId }); fetchExpenses(selected.id); }
    catch (err) { alert(err.response?.data?.message ?? 'Failed'); }
  };

  const handleReject = async (id) => {
    const reason = window.prompt('Rejection reason:');
    if (!reason) return;
    try { await rejectExpense(id, { rejectedById: user.userId, reason }); fetchExpenses(selected.id); }
    catch (err) { alert(err.response?.data?.message ?? 'Failed'); }
  };

  const typeIcon = { FLIGHT: '✈️', HOTEL: '🏨', MEAL: '🍽️', LOCAL_TRANSPORT: '🚕', VISA: '🛂', MISCELLANEOUS: '📦' };

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Expenses</h2>
          <p className="page-subtitle">Track and manage travel expense claims</p>
        </div>
        {selected && isRole('EMPLOYEE','ADMIN') && (
          <button className="btn btn-primary" onClick={() => setShowAdd(true)}>+ Submit Expense</button>
        )}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: 'var(--space-lg)' }}>
        {/* Request selector */}
        <div className="glass-card" style={{ padding: 'var(--space-md)', height: 'fit-content' }}>
          <p className="text-xs text-muted mb-md" style={{ letterSpacing: '0.08em', textTransform: 'uppercase', fontWeight: 600 }}>Booked Requests</p>
          {requests.length === 0 ? (
            <EmptyState icon="📭" title="No booked requests" />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              {requests.map(req => (
                <button key={req.id}
                  onClick={() => setSelected(req)}
                  style={{
                    padding: '10px 12px', borderRadius: 'var(--radius-md)',
                    background: selected?.id === req.id ? 'rgba(59,130,246,0.15)' : 'transparent',
                    border: `1px solid ${selected?.id === req.id ? 'rgba(59,130,246,0.4)' : 'transparent'}`,
                    cursor: 'pointer', textAlign: 'left',
                  }}>
                  <div style={{ fontWeight: 600, fontSize: 13, color: 'var(--color-text-primary)' }}>{req.requestNumber}</div>
                  <div style={{ fontSize: 12, color: 'var(--color-text-muted)', marginTop: 2 }}>{req.fromCity} → {req.toCity}</div>
                  <div style={{ marginTop: 4 }}><StatusBadge status={req.status} /></div>
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Expenses list */}
        <div className="glass-card" style={{ padding: 'var(--space-lg)' }}>
          {!selected ? (
            <EmptyState icon="💰" title="Select a request" />
          ) : loading ? (
            <div className="loading-screen"><div className="spinner spinner-lg" /></div>
          ) : expenses.length === 0 ? (
            <EmptyState icon="📭" title="No expenses yet" desc="Submit expense claims for this trip." />
          ) : (
            <>
              <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 'var(--space-md)' }}>
                <span style={{ fontWeight: 700, color: 'var(--color-text-primary)', fontSize: 'var(--font-size-md)' }}>
                  Total: {formatCurrency(expenses.reduce((s, e) => s + (e.amount ?? 0), 0))}
                </span>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-sm)' }}>
                {expenses.map(exp => (
                  <div key={exp.id} style={{
                    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                    padding: 'var(--space-md)', background: 'rgba(255,255,255,0.03)',
                    border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)'
                  }}>
                    <div style={{ display: 'flex', gap: 'var(--space-md)', alignItems: 'center' }}>
                      <span style={{ fontSize: 24 }}>{typeIcon[exp.expenseType] ?? '📦'}</span>
                      <div>
                        <div style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>
                          {exp.expenseType?.replace(/_/g, ' ')} — {formatCurrency(exp.amount)}
                        </div>
                        <div className="text-sm text-muted">{exp.notes ?? 'No notes'}</div>
                        {exp.receiptUrl && (
                          <a href={exp.receiptUrl} target="_blank" rel="noreferrer" className="text-xs" style={{ color: 'var(--color-primary-light)' }}>
                            📎 Receipt
                          </a>
                        )}
                      </div>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-sm)', flexShrink: 0 }}>
                      <StatusBadge status={exp.status} />
                      {exp.status === 'PENDING' && isRole('FINANCE','ADMIN') && (
                        <>
                          <button className="btn btn-success btn-sm" onClick={() => setVerifyModal(exp)}>Verify</button>
                          <button className="btn btn-danger btn-sm" onClick={() => handleReject(exp.id)}>Reject</button>
                        </>
                      )}
                      {exp.status === 'VERIFIED' && isRole('FINANCE','ADMIN') && (
                        <button className="btn btn-primary btn-sm" onClick={() => handleReimburse(exp.id)}>Reimburse</button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </>
          )}
        </div>
      </div>

      {/* Add Expense Modal */}
      <Modal open={showAdd} onClose={() => setShowAdd(false)} title="💰 Submit Expense"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setShowAdd(false)}>Cancel</button>
          <button className="btn btn-primary" form="exp-form" type="submit" disabled={saving}>{saving ? 'Submitting...' : 'Submit'}</button>
        </>}
      >
        <form id="exp-form" onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div className="form-group">
            <label className="form-label">Expense Type</label>
            <select className="form-select" value={form.expenseType} onChange={e => setForm(f => ({ ...f, expenseType: e.target.value }))}>
              {EXPENSE_TYPES.map(t => <option key={t}>{t}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Amount (₹)</label>
            <input className="form-input" type="number" required min="1" step="1" value={form.amount} onChange={e => setForm(f => ({ ...f, amount: e.target.value }))} placeholder="1500" />
          </div>
          <div className="form-group">
            <label className="form-label">Receipt URL <span className="text-xs text-muted">(required if amount &gt; ₹1,000)</span></label>
            <input className="form-input" type="url" value={form.receiptUrl} onChange={e => setForm(f => ({ ...f, receiptUrl: e.target.value }))} placeholder="https://drive.google.com/..." />
          </div>
          <div className="form-group">
            <label className="form-label">Notes</label>
            <textarea className="form-textarea" value={form.notes} onChange={e => setForm(f => ({ ...f, notes: e.target.value }))} placeholder="Brief description..." />
          </div>
        </form>
      </Modal>

      {/* Verify Modal */}
      <Modal open={!!verifyModal} onClose={() => { setVerifyModal(null); setNotes(''); }} title="✅ Verify Expense"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setVerifyModal(null)}>Cancel</button>
          <button className="btn btn-success" onClick={handleVerify} disabled={saving}>{saving ? 'Verifying...' : 'Verify Expense'}</button>
        </>}
      >
        {verifyModal && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            <div className="alert alert-info">
              ℹ️ Verifying <strong>{verifyModal.expenseType?.replace(/_/g,' ')}</strong> expense of <strong>{formatCurrency(verifyModal.amount)}</strong>
            </div>
            <div className="form-group">
              <label className="form-label">Verification Notes</label>
              <textarea className="form-textarea" value={notes} onChange={e => setNotes(e.target.value)} placeholder="Optional notes..." />
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}
