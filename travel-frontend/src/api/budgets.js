import api from './axios';

export const createBudget         = (data)  => api.post('/budgets', data);
export const getAllBudgets         = ()      => api.get('/budgets/utilization');
export const getBudgetByDept      = (dept)  => api.get(`/budgets/department/${dept}`);
export const getBudgetByCostCenter = (cc)   => api.get(`/budgets/cost-center/${cc}`);
