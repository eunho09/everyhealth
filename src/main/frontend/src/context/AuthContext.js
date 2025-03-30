import React, { createContext, useState, useEffect, useContext, useRef } from 'react';
import { loginService } from '../services/loginService';
import { updateAuthState, setRefreshTokenFunction } from '../services/api';
import axios from 'axios';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [tokenExpiresAt, setTokenExpiresAt] = useState(null);
  const [needTokenRefresh, setNeedTokenRefresh] = useState(false);
  const expirationTimer = useRef(null);

  const setTokenExpiration = (expiresInSeconds = 1800) => {
    const expiresAt = new Date(Date.now() + expiresInSeconds * 1000);
    setTokenExpiresAt(expiresAt);

    if (expirationTimer.current) {
      clearTimeout(expirationTimer.current);
    }

    const timeUntilExpiry = expiresAt.getTime() - Date.now();
    expirationTimer.current = setTimeout(() => {
      console.log("토큰 만료됨, 갱신 필요 상태로 변경");
      setNeedTokenRefresh(true);

      updateAuthState({
        user,
        isAuthenticated: !!user,
        tokenExpiresAt: expiresAt,
        needTokenRefresh: true
      });
    }, timeUntilExpiry);

    console.log(`토큰 만료 시간 설정: ${expiresAt.toLocaleTimeString()}`);
  };

  const refreshToken = async () => {
    try {
      console.log("토큰 갱신 시도");
      const response = await axios.post('/api/token/refresh', {}, {
        withCredentials: true
      });

      if (response.status === 200) {
        console.log("토큰 갱신 성공");

        setNeedTokenRefresh(false);

        return true;
      }

      return false;
    } catch (error) {
      console.error('토큰 갱신 실패:', error);
      return false;
    }
  };

  const checkLoginStatus = async () => {
    try {
      const data = await loginService.loginCheck();

      if (data.check) {
        setUser(data);
        setNeedTokenRefresh(false);

        if (data.expiresIn) {
          setTokenExpiration(data.expiresIn * 60);
        } else {
          setTokenExpiration(1800);
        }

        updateAuthState({
          user: data,
          isAuthenticated: true,
          tokenExpiresAt,
          needTokenRefresh: false
        });

        console.log("로그인 확인 성공");
        return true;
      } else {
        setUser(null);
        setTokenExpiresAt(null);
        setNeedTokenRefresh(false);

        updateAuthState({
          user: null,
          isAuthenticated: false,
          tokenExpiresAt: null,
          needTokenRefresh: false
        });

        return false;
      }
    } catch (error) {
      console.error('로그인 상태 확인 실패:', error);
      setUser(null);
      setTokenExpiresAt(null);
      setNeedTokenRefresh(false);

      updateAuthState({
        user: null,
        isAuthenticated: false,
        tokenExpiresAt: null,
        needTokenRefresh: false
      });

      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      await loginService.logout();

      if (expirationTimer.current) {
        clearTimeout(expirationTimer.current);
        expirationTimer.current = null;
      }

      setUser(null);
      setTokenExpiresAt(null);
      setNeedTokenRefresh(false);

      updateAuthState({
        user: null,
        isAuthenticated: false,
        tokenExpiresAt: null,
        needTokenRefresh: false
      });

      return true;
    } catch (error) {
      console.error('로그아웃 실패:', error);
      return false;
    }
  };

  useEffect(() => {

    setRefreshTokenFunction(refreshToken);

    checkLoginStatus();

    return () => {
      if (expirationTimer.current) {
        clearTimeout(expirationTimer.current);
      }
    };
  }, []);

  useEffect(() => {
    updateAuthState({
      user,
      isAuthenticated: !!user,
      tokenExpiresAt,
      needTokenRefresh
    });
  }, [user, tokenExpiresAt, needTokenRefresh]);

  useEffect(() => {
    const handleAuthError = () => {
      console.log("인증 오류 이벤트 감지 - 로그아웃");

      if (expirationTimer.current) {
        clearTimeout(expirationTimer.current);
        expirationTimer.current = null;
      }

      setUser(null);
      setTokenExpiresAt(null);
      setNeedTokenRefresh(false);

      updateAuthState({
        user: null,
        isAuthenticated: false,
        tokenExpiresAt: null,
        needTokenRefresh: false
      });
    };

    window.addEventListener('auth:unauthorized', handleAuthError);

    return () => {
      window.removeEventListener('auth:unauthorized', handleAuthError);
    };
  }, []);

  const contextValue = {
    user,
    isLoading,
    checkLoginStatus,
    logout,
    tokenExpiresAt,
    needTokenRefresh,
    refreshToken
  };

  return (
      <AuthContext.Provider value={contextValue}>
        {children}
      </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth는 AuthProvider 내부에서만 사용할 수 있습니다.');
  }
  return context;
};