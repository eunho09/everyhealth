import axios from 'axios';

// refreshToken 함수 참조를 위한 변수
let refreshTokenFunction = async () => false;

export const setRefreshTokenFunction = (fn) => {
    refreshTokenFunction = fn;
};

const getBaseUrl = () => {
    if (process.env.ENV === 'dev') {
        console.log(process.env.REACT_API_BASEURL + "BASEURL");
        return process.env.REACT_API_BASEURL;
    }

    console.log(window.env?.REACT_API_BASEURL + "BASEURL");
    return window.env?.REACT_API_BASEURL;
}

const api = axios.create({
    baseURL: getBaseUrl(),
    withCredentials: true,
});

// 인증 상태를 저장할 객체
let authContextValue = {
    isAuthenticated: false,
    user: null,
    tokenExpiresAt: null,
    needTokenRefresh: false
};

export const updateAuthState = (newState) => {
    authContextValue = newState;
};

// API 인터셉터
api.interceptors.request.use(config => {
    const publicUrls = [
        '/api/login/check',
        '/api/token/refresh',
        '/login',
        '/oauth2/authorization/google'
    ];

    // 토큰 갱신이 필요하고 refresh 요청이 아닌 경우 401 에러 발생
    if (authContextValue.needTokenRefresh && !config.url.includes('/api/token/refresh')) {
        console.log("토큰 만료됨, 갱신 필요:", config.url);

        // 401 에러 생성
        const error = new Error('토큰 만료');
        error.response = { status: 401 };
        error.config = config;
        return Promise.reject(error);
    }

    // 인증이 필요한 URL이고 인증되지 않은 상태
    if (!publicUrls.includes(config.url) && !authContextValue.isAuthenticated) {
        console.log("인증되지 않은 요청:", config.url);

        const authErrorEvent = new CustomEvent('auth:unauthorized');
        window.dispatchEvent(authErrorEvent);

        return Promise.reject(new Error('인증되지 않은 요청'));
    }

    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        // axios.Cancel에 의한 취소는 무시
        if (axios.isCancel(error)) {
            return Promise.reject(error);
        }

        const originalRequest = error.config || {};

        // 401 에러 처리 (토큰 만료)
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                // refreshToken 함수 호출
                const refreshSuccess = await refreshTokenFunction();

                if (refreshSuccess) {
                    // 토큰 갱신 성공 - 원래 요청 재시도
                    return axios(originalRequest);
                } else {
                    // 갱신 실패 - 로그아웃 이벤트 발생
                    const authErrorEvent = new CustomEvent('auth:unauthorized');
                    window.dispatchEvent(authErrorEvent);
                }
            } catch (refreshError) {
                console.error('토큰 갱신 실패:', refreshError);
                const authErrorEvent = new CustomEvent('auth:unauthorized');
                window.dispatchEvent(authErrorEvent);
            }
        }

/*        // 500 에러 등 다른 상태 코드 처리
        if (error.response && error.response.status === 500) {
            console.error('서버 오류 발생:', error.response.data);
            alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }*/

        return Promise.reject(error);
    }
);

export default api;