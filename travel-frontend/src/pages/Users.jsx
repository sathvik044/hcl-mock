import React, { useEffect, useState } from 'react';
import { getUsers, createUser } from '../api/users';
import { RoleBadge, Modal, EmptyState } from '../components/UI';

const ROLES = ['ADMIN','MANAGER','EMPLOYEE','FINANCE','TRAVEL_DESK'];

export default function Users() {
  const [users,   setUsers]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [page,    setPage]    = useState(0);
  const [total,   setTotal]   = useState(0);
  const [showAdd, setShowAdd] = useState(false);
  const [saving,  setSaving]  = useState(false);
  const [form, setForm] = useState({
    name: '', email: '', password: 'Password@123', role: 'EMPLOYEE',
    department: '', costCenter: '', managerId: ''
  });

  useEffect(() => { fetchUsers(); }, [page]);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getUsers({ page, size: 12 });
      const data = Array.isArray(res.data) ? res.data : [];
      setUsers(data);
      setTotal(data.length);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const payload = { ...form };
      delete payload.password;
      if (!payload.managerId) delete payload.managerId;
      await createUser(payload);
      setShowAdd(false);
      setForm({ name: '', email: '', password: 'Password@123', role: 'EMPLOYEE', department: '', costCenter: '', managerId: '' });
      fetchUsers();
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to create user'); }
    finally { setSaving(false); }
  };

  const roleColors = { ADMIN: 'amber', MANAGER: 'purple', EMPLOYEE: 'blue', FINANCE: 'green', TRAVEL_DESK: 'pink' };

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">User Management</h2>
          <p className="page-subtitle">{total} users in the system</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowAdd(true)}>+ Add User</button>
      </div>

      {loading ? (
        <div className="loading-screen"><div className="spinner spinner-lg" /></div>
      ) : users.length === 0 ? (
        <EmptyState icon="👥" title="No users found" />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-md)' }}>
          {users.map(u => (
            <div key={u.id} className="glass-card" style={{ padding: 'var(--space-lg)', transition: 'all 0.2s' }}
              onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-3px)'}
              onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0)'}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-md)', marginBottom: 'var(--space-md)' }}>
                <div style={{
                  width: 48, height: 48, borderRadius: '50%',
                  background: 'var(--gradient-primary)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: 20, fontWeight: 700, color: '#fff', flexShrink: 0,
                }}>
                  {u.name?.charAt(0)?.toUpperCase()}
                </div>
                <div style={{ overflow: 'hidden' }}>
                  <div style={{ fontWeight: 700, color: 'var(--color-text-primary)', fontSize: 'var(--font-size-md)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{u.name}</div>
                  <div className="text-xs text-muted" style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{u.email}</div>
                </div>
              </div>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                <RoleBadge role={u.role} />
                <span className="badge badge-draft">{u.department}</span>
              </div>
              <div className="text-xs text-muted mt-md">{u.costCenter}</div>
            </div>
          ))}
        </div>
      )}

      {total > 12 && (
        <div className="flex items-center justify-between mt-lg">
          <span className="text-sm text-muted">Page {page + 1} of {Math.ceil(total / 12)}</span>
          <div className="flex gap-sm">
            <button className="btn btn-ghost btn-sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Prev</button>
            <button className="btn btn-ghost btn-sm" disabled={(page + 1) * 12 >= total} onClick={() => setPage(p => p + 1)}>Next →</button>
          </div>
        </div>
      )}

      {/* Add User Modal */}
      <Modal open={showAdd} onClose={() => setShowAdd(false)} title="👤 Add New User"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setShowAdd(false)}>Cancel</button>
          <button className="btn btn-primary" form="user-form" type="submit" disabled={saving}>{saving ? 'Creating...' : 'Create User'}</button>
        </>}
      >
        <form id="user-form" onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Full Name</label>
              <input className="form-input" required value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} placeholder="John Doe" />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" required value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} placeholder="john@corporate.com" />
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Role</label>
              <select className="form-select" value={form.role} onChange={e => setForm(f => ({ ...f, role: e.target.value }))}>
                {ROLES.map(r => <option key={r}>{r}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Department</label>
              <input className="form-input" required value={form.department} onChange={e => setForm(f => ({ ...f, department: e.target.value }))} placeholder="Engineering" />
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Cost Center</label>
              <input className="form-input" required value={form.costCenter} onChange={e => setForm(f => ({ ...f, costCenter: e.target.value }))} placeholder="CC-ENG-001" />
            </div>
            <div className="form-group">
              <label className="form-label">Manager ID <span className="text-muted">(optional)</span></label>
              <input className="form-input" type="number" value={form.managerId} onChange={e => setForm(f => ({ ...f, managerId: e.target.value }))} placeholder="1" />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input className="form-input" type="password" required value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
          </div>
        </form>
      </Modal>
    </div>
  );
}
