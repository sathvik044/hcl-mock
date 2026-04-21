import api from './axios';

export const getPendingManagerApprovals = (managerId) => api.get(`/api/approvals/pending/manager/${managerId}`);
export const getPendingFinanceApprovals = (params)    => api.get('/api/approvals/pending/finance', { params });
