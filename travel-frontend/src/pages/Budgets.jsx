import React, { useEffect, useState } from 'react';
import { getAllBudgets, createBudget } from '../api/budgets';
import { Modal, EmptyState, formatCurrency } from '../components/UI';

const DEPARTMENTS = ['Engineering','Sales','Finance','IT','Operations','HR','Marketing'];

export default function Budgets() {
  const [budgets,  setBudgets]  = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [showAdd,  setShowAdd]  = useState(false);
  const [saving,   setSaving]   = useState(false);
  const [form, setForm] = useState({ department: 'Engineering', costCenter: '', financialYear: new Date().getFullYear(), totalAllocated: '' });

  useEffect(() => { fetchBudgets(); }, []);

  const fetchBudgets = async () => {
    setLoading(true);
    try {
      const res = await getAllBudgets();
      setBudgets(res.data.data ?? []);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await createBudget({ ...form, totalAllocated: parseFloat(form.totalAllocated) });
      setShowAdd(false);
      setForm({ department: 'Engineering', costCenter: '', financialYear: new Date().getFullYear(), totalAllocated: '' });
      fetchBudgets();
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to create budget'); }
    finally { setSaving(false); }
  };

  const totalAllocated  = budgets.reduce((s, b) => s + (b.totalAllocated ?? 0), 0);
  const totalUtilized   = budgets.reduce((s, b) => s + (b.totalUtilized ?? 0), 0);
  const avgUtil         = totalAllocated > 0 ? Math.round((totalUtilized / totalAllocated) * 100) : 0;

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Budget Management</h2>
          <p className="page-subtitle">Department-wise budget allocation and utilization</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowAdd(true)}>+ Add Budget</button>
      </div>

      {/* Summary */}
      <div className="grid-3 mb-lg">
        {[
          { label: 'Total Allocated', val: formatCurrency(totalAllocated), color: 'blue', icon: '💼' },
          { label: 'Total Utilized',  val: formatCurrency(totalUtilized),  color: 'amber', icon: '📉' },
          { label: 'Avg Utilization', val: `${avgUtil}%`,                  color: avgUtil >= 80 ? 'pink' : 'green', icon: '📊' },
        ].map(s => (
          <div key={s.label} className={`stat-card ${s.color}`}>
            <div className={`stat-icon ${s.color}`}>{s.icon}</div>
            <div className="stat-value">{s.val}</div>
            <div className="stat-label">{s.label}</div>
          </div>
        ))}
      </div>

      {loading ? (
        <div className="loading-screen"><div className="spinner spinner-lg" /></div>
      ) : budgets.length === 0 ? (
        <EmptyState icon="📈" title="No budget records" desc="Create your first department budget." />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: 'var(--space-md)' }}>
          {budgets.map(b => {
            const pct   = b.totalAllocated > 0 ? Math.round((b.totalUtilized / b.totalAllocated) * 100) : 0;
            const color = pct >= 90 ? 'red' : pct >= 70 ? 'amber' : 'green';
            return (
              <div key={b.id} className="glass-card" style={{ padding: 'var(--space-lg)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 'var(--space-md)' }}>
                  <div>
                    <div style={{ fontWeight: 700, fontSize: 'var(--font-size-md)', color: 'var(--color-text-primary)' }}>{b.department}</div>
                    <div className="text-sm text-muted">FY {b.financialYear} · {b.costCenter}</div>
                  </div>
                  <span style={{ fontSize: 24, fontWeight: 800, color: pct >= 90 ? '#f87171' : pct >= 70 ? '#fcd34d' : '#6ee7b7' }}>
                    {pct}%
                  </span>
                </div>

                <div className="progress-bar mb-md">
                  <div className={`progress-fill ${color}`} style={{ width: `${Math.min(pct, 100)}%` }} />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 'var(--space-sm)' }}>
                  {[
                    ['Allocated', formatCurrency(b.totalAllocated)],
                    ['Utilized',  formatCurrency(b.totalUtilized)],
                    ['Remaining', formatCurrency(b.remainingBalance)],
                  ].map(([k, v]) => (
                    <div key={k} style={{ textAlign: 'center', padding: '8px', background: 'rgba(255,255,255,0.03)', borderRadius: 'var(--radius-sm)' }}>
                      <div className="text-xs text-muted">{k}</div>
                      <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginTop: 2 }}>{v}</div>
                    </div>
                  ))}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Add Budget Modal */}
      <Modal open={showAdd} onClose={() => setShowAdd(false)} title="📈 Add Department Budget"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setShowAdd(false)}>Cancel</button>
          <button className="btn btn-primary" form="budget-form" type="submit" disabled={saving}>{saving ? 'Creating...' : 'Create Budget'}</button>
        </>}
      >
        <form id="budget-form" onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div className="form-group">
            <label className="form-label">Department</label>
            <select className="form-select" value={form.department} onChange={e => setForm(f => ({ ...f, department: e.target.value }))}>
              {DEPARTMENTS.map(d => <option key={d}>{d}</option>)}
            </select>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Cost Center</label>
              <input className="form-input" required value={form.costCenter} onChange={e => setForm(f => ({ ...f, costCenter: e.target.value }))} placeholder="CC-ENG-001" />
            </div>
            <div className="form-group">
              <label className="form-label">Financial Year</label>
              <input className="form-input" type="number" required value={form.financialYear} onChange={e => setForm(f => ({ ...f, financialYear: parseInt(e.target.value) }))} />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Total Allocated (₹)</label>
            <input className="form-input" type="number" required min="0" step="10000" value={form.totalAllocated} onChange={e => setForm(f => ({ ...f, totalAllocated: e.target.value }))} placeholder="500000" />
          </div>
        </form>
      </Modal>
    </div>
  );
}
