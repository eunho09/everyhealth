import React, { createContext, useState, useEffect, useContext } from 'react';
import { loginService } from '../services/loginService';
import { setAuthState } from '../services/api';


export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const checkLoginStatus = async () => {
    try {
      const data = await loginService.loginCheck();

      if (data.check) {
        setUser(data);
        setAuthState(true);
        console.log("로그인 확인 성공");
      } else {
        setUser(null);
        setAuthState(false);
      }
    } catch (error) {
      console.error('로그인 상태 확인 실패:', error);
      setUser(null);
      setAuthState(false);
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      await loginService.logout();
      setUser(null);
      setAuthState(false);
      return true;
    } catch (error) {
      console.error('로그아웃 실패:', error);
      return false;
    }
  };


  useEffect(() => {
    checkLoginStatus();
  }, []);

  const contextValue = {
    user,
    isLoading,
    checkLoginStatus,
    logout
  };

  return (
      <AuthContext.Provider value={contextValue}>
        {children}
      </AuthContext.Provider>
  );
};

// 커스텀 훅으로 Auth Context 사용 간소화
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth는 AuthProvider 내부에서만 사용할 수 있습니다.');
  }
  return context;
};