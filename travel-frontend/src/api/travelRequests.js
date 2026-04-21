import api from './axios';

export const getTravelRequests     = (params) => api.get('/api/travel-requests', { params });
export const getTravelRequestById  = (id)     => api.get(`/api/travel-requests/${id}`);
export const getMyRequests         = (empId, params) => api.get(`/api/travel-requests/employee/${empId}`, { params });
export const createTravelRequest   = (data)   => api.post('/api/travel-requests', data);
export const submitRequest         = (id, actorId) => api.put(`/api/travel-requests/${id}/submit?actorId=${actorId}`);
export const cancelRequest         = (id, actorId) => api.put(`/api/travel-requests/${id}/cancel?actorId=${actorId}`);
export const getBookedRequests     = (params) => api.get('/api/travel-requests/booked', { params });
export const bookRequest           = (id, data)   => api.put(`/api/travel-requests/${id}/book`, data);
export const managerApprove        = (id, data)   => api.put(`/api/travel-requests/${id}/manager-approve`, data);
export const managerReject         = (id, data)   => api.put(`/api/travel-requests/${id}/manager-reject`, data);
export const financeApprove        = (id, data)   => api.put(`/api/travel-requests/${id}/finance-approve`, data);
export const financeReject         = (id, data)   => api.put(`/api/travel-requests/${id}/finance-reject`, data);
