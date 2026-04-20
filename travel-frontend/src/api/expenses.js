import api from './axios';

export const submitExpense    = (data) => api.post('/api/expenses', data);
export const getExpensesByReq = (id)   => api.get(`/api/expenses/travel-request/${id}`);
export const verifyExpense    = (id, data) => api.put(`/api/expenses/${id}/verify`, data);
export const reimburseExpense = (id, data) => api.put(`/api/expenses/${id}/reimburse`, data);
export const rejectExpense    = (id, data) => api.put(`/api/expenses/${id}/reject`, data);
