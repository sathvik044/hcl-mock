import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { getTravelRequests } from '../api/travelRequests';
import { getByRequest, addSegment, confirmSegment, cancelSegment } from '../api/itineraries';
import { StatusBadge, Modal, EmptyState, formatDate } from '../components/UI';

const SEGMENT_TYPES = ['FLIGHT', 'HOTEL', 'CAB', 'TRAIN', 'BUS', 'OTHER'];

export default function Itinerary() {
  const { user } = useAuth();
  const [requests, setRequests] = useState([]);
  const [selected, setSelected] = useState(null);
  const [segments, setSegments] = useState([]);
  const [loadingReqs, setLoadingReqs] = useState(true);
  const [loadingSeg, setLoadingSeg] = useState(false);
  const [showAdd, setShowAdd] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    travelRequestId: '', segmentType: 'FLIGHT', origin: '', destination: '',
    departureTime: '', arrivalTime: '', carrierOrVendor: '', bookingReference: '', cost: ''
  });

  useEffect(() => { fetchRequests(); }, []);
  useEffect(() => { if (selected) fetchSegments(selected.id); }, [selected]);

  const fetchRequests = async () => {
    try {
      const res = await getTravelRequests({ size: 100 });
      const filtered = (res.data.data?.content ?? []).filter(r =>
        ['MANAGER_APPROVED', 'FINANCE_APPROVED', 'BOOKED', 'COMPLETED'].includes(r.status));
      setRequests(filtered);
      if (filtered.length > 0) setSelected(filtered[0]);
    } catch (e) { console.error(e); }
    finally { setLoadingReqs(false); }
  };

  const fetchSegments = async (reqId) => {
    setLoadingSeg(true);
    try {
      const res = await getByRequest(reqId);
      setSegments(res.data.data ?? []);
    } catch (e) { setSegments([]); }
    finally { setLoadingSeg(false); }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await addSegment({ ...form, travelRequestId: selected.id, cost: parseFloat(form.cost) });
      setShowAdd(false);
      setForm({ travelRequestId: '', segmentType: 'FLIGHT', origin: '', destination: '', departureTime: '', arrivalTime: '', carrierOrVendor: '', bookingReference: '', cost: '' });
      fetchSegments(selected.id);
    } catch (err) { alert(err.response?.data?.message ?? 'Failed to add segment'); }
    finally { setSaving(false); }
  };

  const handleConfirm = async (id) => {
    try { await confirmSegment(id); fetchSegments(selected.id); }
    catch (err) { alert(err.response?.data?.message ?? 'Failed'); }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('Cancel this segment?')) return;
    try { await cancelSegment(id); fetchSegments(selected.id); }
    catch (err) { alert(err.response?.data?.message ?? 'Failed'); }
  };

  const segTypeIcon = { FLIGHT: '✈️', HOTEL: '🏨', CAB: '🚕', TRAIN: '🚆', BUS: '🚌', OTHER: '📦' };

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <div>
          <h2 className="page-title">Itinerary</h2>
          <p className="page-subtitle">Manage travel segments for approved requests</p>
        </div>
        {selected && (
          <button className="btn btn-primary" onClick={() => setShowAdd(true)}>+ Add Segment</button>
        )}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: 'var(--space-lg)' }}>
        {/* Request selector */}
        <div className="glass-card" style={{ padding: 'var(--space-md)', height: 'fit-content' }}>
          <p className="text-xs text-muted mb-md" style={{ letterSpacing: '0.08em', textTransform: 'uppercase', fontWeight: 600 }}>Requests</p>
          {loadingReqs ? (
            <div className="loading-screen" style={{ minHeight: 100 }}><div className="spinner" /></div>
          ) : requests.length === 0 ? (
            <EmptyState icon="✈️" title="No approved requests" desc="Requests must be approved before adding segments." />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              {requests.map(req => (
                <button key={req.id}
                  onClick={() => setSelected(req)}
                  style={{
                    padding: '10px 12px', borderRadius: 'var(--radius-md)',
                    background: selected?.id === req.id ? 'rgba(59,130,246,0.15)' : 'transparent',
                    border: `1px solid ${selected?.id === req.id ? 'rgba(59,130,246,0.4)' : 'transparent'}`,
                    cursor: 'pointer', textAlign: 'left', transition: 'all 0.15s',
                  }}>
                  <div style={{ fontWeight: 600, fontSize: 13, color: 'var(--color-text-primary)' }}>{req.requestNumber}</div>
                  <div style={{ fontSize: 12, color: 'var(--color-text-muted)', marginTop: 2 }}>{req.fromCity} → {req.toCity}</div>
                  <div style={{ marginTop: 4 }}><StatusBadge status={req.status} /></div>
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Segments */}
        <div className="glass-card" style={{ padding: 'var(--space-lg)' }}>
          {!selected ? (
            <EmptyState icon="🗺️" title="Select a request" />
          ) : loadingSeg ? (
            <div className="loading-screen"><div className="spinner spinner-lg" /></div>
          ) : segments.length === 0 ? (
            <EmptyState icon="📭" title="No segments yet" desc="Add flight, hotel, or cab segments to build the itinerary." />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-md)' }}>
              {segments.map(seg => (
                <div key={seg.id} style={{
                  display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between',
                  padding: 'var(--space-md)', background: 'rgba(255,255,255,0.03)',
                  border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)'
                }}>
                  <div style={{ display: 'flex', gap: 'var(--space-md)', alignItems: 'flex-start' }}>
                    <span style={{ fontSize: 28 }}>{segTypeIcon[seg.segmentType] ?? '📦'}</span>
                    <div>
                      <div style={{ fontWeight: 700, color: 'var(--color-text-primary)', marginBottom: 4 }}>
                        {seg.segmentType} — {seg.origin} → {seg.destination}
                      </div>
                      <div className="text-sm text-muted">
                        {seg.carrierOrVendor && <span>{seg.carrierOrVendor} · </span>}
                        {seg.bookingReference && <span>Ref: {seg.bookingReference} · </span>}
                        {formatDate(seg.departureTime)} – {formatDate(seg.arrivalTime)}
                      </div>
                    </div>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-sm)', flexShrink: 0 }}>
                    <StatusBadge status={seg.status} />
                    {seg.status === 'PENDING' && (
                      <>
                        <button className="btn btn-success btn-sm" onClick={() => handleConfirm(seg.id)}>Confirm</button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleCancel(seg.id)}>Cancel</button>
                      </>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Add Segment Modal */}
      <Modal open={showAdd} onClose={() => setShowAdd(false)} title="🗺️ Add Itinerary Segment"
        footer={<>
          <button className="btn btn-ghost" onClick={() => setShowAdd(false)}>Cancel</button>
          <button className="btn btn-primary" form="seg-form" type="submit" disabled={saving}>
            {saving ? 'Adding...' : 'Add Segment'}
          </button>
        </>}
      >
        <form id="seg-form" onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div className="form-group">
            <label className="form-label">Segment Type</label>
            <select className="form-select" value={form.segmentType} onChange={e => setForm(f => ({ ...f, segmentType: e.target.value }))}>
              {SEGMENT_TYPES.map(t => <option key={t}>{t}</option>)}
            </select>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Origin</label>
              <input className="form-input" required value={form.origin} onChange={e => setForm(f => ({ ...f, origin: e.target.value }))} placeholder="e.g. BLR" />
            </div>
            <div className="form-group">
              <label className="form-label">Destination</label>
              <input className="form-input" required value={form.destination} onChange={e => setForm(f => ({ ...f, destination: e.target.value }))} placeholder="e.g. BOM" />
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Departure</label>
              <input className="form-input" type="datetime-local" value={form.departureTime} onChange={e => setForm(f => ({ ...f, departureTime: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Arrival</label>
              <input className="form-input" type="datetime-local" value={form.arrivalTime} onChange={e => setForm(f => ({ ...f, arrivalTime: e.target.value }))} />
            </div>
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Carrier / Vendor</label>
              <input className="form-input" value={form.carrierOrVendor} onChange={e => setForm(f => ({ ...f, carrierOrVendor: e.target.value }))} placeholder="e.g. IndiGo" />
            </div>
            <div className="form-group">
              <label className="form-label">Booking Reference</label>
              <input className="form-input" value={form.bookingReference} onChange={e => setForm(f => ({ ...f, bookingReference: e.target.value }))} placeholder="e.g. 6E-123" />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Cost (₹)</label>
            <input className="form-input" type="number" required min="0" step="100" value={form.cost} onChange={e => setForm(f => ({ ...f, cost: e.target.value }))} placeholder="5000" />
          </div>
        </form>
      </Modal>
    </div>
  );
}
