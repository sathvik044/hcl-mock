import api from './axios';

export const addSegment      = (data) => api.post('/api/itineraries', data);
export const getByRequest    = (id)   => api.get(`/api/itineraries/travel-request/${id}`);
export const confirmSegment  = (id)   => api.put(`/api/itineraries/${id}/confirm`);
export const cancelSegment   = (id)   => api.put(`/api/itineraries/${id}/cancel`);
