import axios from 'axios';

const api = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true,
});

let globalAuthState = localStorage.getItem('auth_state') === "true";

export const setAuthState = (state) => {
    globalAuthState = state;
    localStorage.setItem('auth_state', state ? "true" : "false");
};

// API 인터셉터
api.interceptors.request.use(config => {
    const publicUrls = [
        '/api/login/check',
        '/api/token/refresh',
        '/login',
        '/oauth2/authorization/google'
    ];

    // 인증이 필요한 URL이고 인증되지 않은 상태
    if (!publicUrls.includes(config.url) && !globalAuthState) {
        // 리다이렉트를 React Router로 처리하도록 이벤트 발생
        const authErrorEvent = new CustomEvent('auth:unauthorized');
        window.dispatchEvent(authErrorEvent);

        return Promise.reject(new axios.CanceledError('인증되지 않은 요청'));
    }
    return config;
});

api.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        // axios.Cancel에 의한 취소는 무시
        if (axios.isCancel(error)) {
            return Promise.reject(error);
        }

        const originalRequest = error.config;

        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                await axios.post('/api/token/refresh');
                localStorage.setItem('auth_state', "true");
                return axios(originalRequest);
            } catch (refreshError) {
                // 토큰 갱신 실패 - 이벤트 발생
                const authErrorEvent = new CustomEvent('auth:unauthorized');
                window.dispatchEvent(authErrorEvent);
                return Promise.reject(error);
            }
        }

        // 500 에러 등 다른 상태 코드도 처리 가능
        if (error.response && error.response.status === 500) {
            console.error('서버 오류 발생:', error.response.data);
            alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }

        return Promise.reject(error);
    }
);

// API 오류 이벤트 발생 유틸리티 함수
export const triggerAuthError = () => {
    const authErrorEvent = new CustomEvent('auth:unauthorized');
    window.dispatchEvent(authErrorEvent);
};

export default api;