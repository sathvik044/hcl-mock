import api from './axios';

export const getPendingManagerApprovals = () =>
  api.get('/travel-requests').then((res) => ({
    ...res,
    data: (res.data ?? []).filter((request) => request.status === 'SUBMITTED'),
  }));

export const getPendingFinanceApprovals = () =>
  api.get('/travel-requests').then((res) => ({
    ...res,
    data: (res.data ?? []).filter((request) => request.status === 'MANAGER_APPROVED'),
  }));
