import api from './axios';

export const getUsers     = (params) => api.get('/api/users', { params });
export const getUserById  = (id)     => api.get(`/api/users/${id}`);
export const createUser   = (data)   => api.post('/api/users', data);
export const getTeam      = (id)     => api.get(`/api/users/${id}/team`);
export const assignManager = (uid, mid) => api.put(`/api/users/${uid}/manager/${mid}`);
