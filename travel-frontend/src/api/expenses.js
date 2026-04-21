import api from './axios';

export const submitExpense    = (data) => api.post('/expenses', data);
export const getExpensesByReq = (id)   => api.get(`/expenses/travel-request/${id}`);
export const verifyExpense    = (id, financeUserId) => api.put(`/expenses/${id}/verify?financeUserId=${financeUserId}`);
export const reimburseExpense = (id) => api.put(`/expenses/${id}/reimburse`);
export const rejectExpense    = (id) => api.put(`/expenses/${id}/reject`);
