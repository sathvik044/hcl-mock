import api from './axios';

export const addSegment      = (data) => api.post('/itineraries', data);
export const getByRequest    = (id)   => api.get(`/itineraries/travel-request/${id}`);
export const confirmSegment  = (id)   => api.put(`/itineraries/${id}/confirm`);
export const cancelSegment   = ()     => Promise.reject(new Error('Cancel itinerary endpoint is not available in backend'));
