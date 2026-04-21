import api from './axios';

export const getUsers     = (params) => api.get('/users', { params });
export const getUserById  = (id)     => api.get(`/users/${id}`);
export const createUser   = (data)   => api.post('/users', data);
export const getTeam      = (id)     => api.get(`/users/${id}/team`);
export const assignManager = () => Promise.reject(new Error('Assign manager endpoint is not available in backend'));
