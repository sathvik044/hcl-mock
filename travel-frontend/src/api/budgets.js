import api from './axios';

export const createBudget         = (data)  => api.post('/api/budgets', data);
export const getAllBudgets         = ()      => api.get('/api/budgets/utilization');
export const getBudgetByDept      = (dept)  => api.get(`/api/budgets/department/${dept}`);
export const getBudgetByCostCenter = (cc)   => api.get(`/api/budgets/cost-center/${cc}`);
