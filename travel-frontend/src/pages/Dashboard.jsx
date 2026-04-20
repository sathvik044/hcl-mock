import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { getTravelRequests, getMyRequests } from '../api/travelRequests';
import { getPendingManagerApprovals, getPendingFinanceApprovals } from '../api/approvals';
import { getAllBudgets } from '../api/budgets';
import { StatCard, formatCurrency } from '../components/UI';

export default function Dashboard() {
  const { user, isRole } = useAuth();
  const [stats, setStats]   = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const results = {};

      if (isRole('ADMIN')) {
        const [all, budgets] = await Promise.all([
          getTravelRequests({ size: 1 }),
          getAllBudgets(),
        ]);
        results.totalRequests = all.data.data?.totalElements ?? 0;
        results.budgets       = budgets.data.data ?? [];
      } else if (isRole('MANAGER')) {
        const [pending, myTeam] = await Promise.all([
          getPendingManagerApprovals(user.userId),
          getMyRequests(user.userId, { size: 100 }),
        ]);
        results.pendingApprovals = pending.data.data?.length ?? 0;
        results.teamRequests     = myTeam.data.data?.totalElements ?? 0;
      } else if (isRole('FINANCE')) {
        const [pending, budgets] = await Promise.all([
          getPendingFinanceApprovals({ size: 100 }),
          getAllBudgets(),
        ]);
        results.pendingFinance = pending.data.data?.totalElements ?? 0;
        results.budgets        = budgets.data.data ?? [];
      } else if (isRole('EMPLOYEE')) {
        const mine = await getMyRequests(user.userId, { size: 100 });
        const list = mine.data.data?.content ?? [];
        results.myTotal    = list.length;
        results.myDraft    = list.filter(r => r.status === 'DRAFT').length;
        results.mySubmitted = list.filter(r => r.status === 'SUBMITTED').length;
        results.myApproved = list.filter(r => ['MANAGER_APPROVED','FINANCE_APPROVED','BOOKED'].includes(r.status)).length;
      } else if (isRole('TRAVEL_DESK')) {
        const booked = await getTravelRequests({ status: 'BOOKED', size: 1 });
        results.booked = booked.data.data?.totalElements ?? 0;
      }

      setStats(results);
    } catch (err) {
      console.error('Dashboard fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  const totalBudget    = (stats.budgets ?? []).reduce((s, b) => s + (b.totalAllocated ?? 0), 0);
  const usedBudget     = (stats.budgets ?? []).reduce((s, b) => s + (b.totalUtilized ?? 0), 0);
  const avgUtilization = totalBudget > 0 ? Math.round((usedBudget / totalBudget) * 100) : 0;

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Welcome back, {user?.name?.split(' ')[0]} 👋</h2>
          <p className="page-subtitle">{user?.department} · {user?.role} · {new Date().toLocaleDateString('en-IN', { weekday: 'long', day: 'numeric', month: 'long' })}</p>
        </div>
      </div>

      {loading ? (
        <div className="loading-screen"><div className="spinner spinner-lg" /><span>Loading dashboard...</span></div>
      ) : (
        <>
          {/* ADMIN */}
          {isRole('ADMIN') && (
            <div className="grid-4 mb-lg">
              <StatCard icon="✈️" label="Total Requests" value={stats.totalRequests} color="blue" />
              <StatCard icon="💼" label="Total Budget"   value={formatCurrency(totalBudget)} color="amber" />
              <StatCard icon="📊" label="Budget Used"    value={formatCurrency(usedBudget)} color="purple" />
              <StatCard icon="📈" label="Avg Utilization" value={`${avgUtilization}%`} color="green" />
            </div>
          )}

          {/* MANAGER */}
          {isRole('MANAGER') && (
            <div className="grid-3 mb-lg">
              <StatCard icon="📋" label="Pending Approvals" value={stats.pendingApprovals} color="amber" subtitle="Awaiting your action" />
              <StatCard icon="👥" label="Team Requests"     value={stats.teamRequests}     color="blue" />
              <StatCard icon="📊" label="Department"        value={user?.department}        color="purple" />
            </div>
          )}

          {/* EMPLOYEE */}
          {isRole('EMPLOYEE') && (
            <div className="grid-4 mb-lg">
              <StatCard icon="📄" label="My Requests"  value={stats.myTotal}     color="blue" />
              <StatCard icon="✏️"  label="Drafts"      value={stats.myDraft}     color="amber" />
              <StatCard icon="📤" label="Submitted"    value={stats.mySubmitted} color="purple" />
              <StatCard icon="✅" label="Approved"     value={stats.myApproved}  color="green" />
            </div>
          )}

          {/* FINANCE */}
          {isRole('FINANCE') && (
            <div className="grid-3 mb-lg">
              <StatCard icon="💰" label="Pending Finance Approvals" value={stats.pendingFinance} color="amber" subtitle="Awaiting finance review" />
              <StatCard icon="🏦" label="Total Budget Allocated"    value={formatCurrency(totalBudget)} color="blue" />
              <StatCard icon="📉" label="Total Budget Utilized"     value={formatCurrency(usedBudget)}  color="green" />
            </div>
          )}

          {/* TRAVEL_DESK */}
          {isRole('TRAVEL_DESK') && (
            <div className="grid-3 mb-lg">
              <StatCard icon="🎟️" label="Booked Requests"  value={stats.booked}    color="blue" />
              <StatCard icon="🏢" label="Department"        value={user?.department} color="purple" />
              <StatCard icon="🗺️" label="Role"              value="Travel Desk"      color="green" />
            </div>
          )}

          {/* Budget utilization cards (ADMIN, FINANCE) */}
          {(isRole('ADMIN','FINANCE')) && (stats.budgets ?? []).length > 0 && (
            <div className="glass-card" style={{ padding: 'var(--space-lg)', marginTop: 'var(--space-lg)' }}>
              <h3 style={{ fontSize: 'var(--font-size-md)', fontWeight: 700, marginBottom: 'var(--space-md)', color: 'var(--color-text-primary)' }}>
                📊 Department Budget Utilization
              </h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-md)' }}>
                {(stats.budgets ?? []).map(b => {
                  const pct = b.totalAllocated > 0 ? Math.round((b.totalUtilized / b.totalAllocated) * 100) : 0;
                  const color = pct >= 90 ? 'red' : pct >= 70 ? 'amber' : 'green';
                  return (
                    <div key={b.id}>
                      <div className="flex justify-between mb-sm">
                        <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{b.department}</span>
                        <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-muted)' }}>{formatCurrency(b.totalUtilized)} / {formatCurrency(b.totalAllocated)} ({pct}%)</span>
                      </div>
                      <div className="progress-bar">
                        <div className={`progress-fill ${color}`} style={{ width: `${Math.min(pct, 100)}%` }} />
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}

          {/* Quick actions */}
          <div className="glass-card" style={{ padding: 'var(--space-lg)', marginTop: 'var(--space-lg)' }}>
            <h3 style={{ fontSize: 'var(--font-size-md)', fontWeight: 700, marginBottom: 'var(--space-md)', color: 'var(--color-text-primary)' }}>
              ⚡ Quick Actions
            </h3>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-sm)' }}>
              <a href="/travel-requests" className="btn btn-primary btn-sm">✈️ Travel Requests</a>
              {isRole('MANAGER','ADMIN','FINANCE') && <a href="/approvals" className="btn btn-ghost btn-sm">✅ Approvals</a>}
              {isRole('ADMIN','TRAVEL_DESK','MANAGER','FINANCE') && <a href="/itinerary" className="btn btn-ghost btn-sm">🗺️ Itinerary</a>}
              <a href="/expenses" className="btn btn-ghost btn-sm">💰 Expenses</a>
              {isRole('ADMIN','FINANCE','MANAGER') && <a href="/budgets" className="btn btn-ghost btn-sm">📈 Budgets</a>}
            </div>
          </div>
        </>
      )}
    </div>
  );
}
