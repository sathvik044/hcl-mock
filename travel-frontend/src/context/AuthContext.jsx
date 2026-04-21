import React, { createContext, useContext, useState, useEffect } from 'react';
import { login as loginApi } from '../api/auth';

const AuthContext = createContext({});

export function AuthProvider({ children }) {
  const [user, setUser]     = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token    = localStorage.getItem('jwt_token');
    const userInfo = localStorage.getItem('user_info');
    if (token && userInfo) {
      try {
        const parsed = JSON.parse(userInfo);
        if (parsed.id && !parsed.userId) parsed.userId = parsed.id;
        setUser(parsed);
      } catch { localStorage.clear(); }
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const res  = await loginApi(email, password);
    const data = res.data.data; // ApiResponse<AuthResponse>
    const userWithId = { ...data, userId: data.id };
    localStorage.setItem('jwt_token', data.token);
    localStorage.setItem('user_info', JSON.stringify(userWithId));
    setUser(userWithId);
    return userWithId;
  };

  const logout = () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
    setUser(null);
  };

  const isRole = (...roles) => user && roles.includes(user.role);

  return (
    <AuthContext.Provider value={{ user, login, logout, loading, isRole }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
