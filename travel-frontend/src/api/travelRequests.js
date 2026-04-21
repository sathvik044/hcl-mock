import api from './axios';

export const getTravelRequests     = (params) => api.get('/travel-requests', { params });
export const getTravelRequestById  = (id)     => api.get(`/travel-requests/${id}`);
export const getMyRequests         = (empId, params) => api.get(`/travel-requests/employee/${empId}`, { params });
export const createTravelRequest   = (data)   => api.post('/travel-requests', data);
export const submitRequest         = (id) => api.put(`/travel-requests/${id}/submit`);
export const cancelRequest         = (id) => api.put(`/travel-requests/${id}/cancel`);
export const getBookedRequests     = (params) => api.get('/travel-requests/booked', { params });
export const bookRequest           = (id, data)   => api.put(`/travel-requests/${id}/book`, data);
export const managerApprove        = (id)   => api.put(`/travel-requests/${id}/approve/manager`);
export const managerReject         = (id)   => api.put(`/travel-requests/${id}/reject/manager`);
export const financeApprove        = (id)   => api.put(`/travel-requests/${id}/approve/finance`);
export const financeReject         = (id)   => api.put(`/travel-requests/${id}/reject/finance`);
