import axios from 'axios';

const api = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true,
});

api.interceptors.response.use(
    (response) => {
        // 응답이 성공적일 때 그대로 반환
        console.log("response", response)
        return response;
    },
    (error) => {
        // 백엔드 응답의 상태 코드가 401일 때 처리
        if (error.response && error.response.status === 401) {
            console.log('Unauthorized error detected. Redirecting to login.');
            window.location.href = '/login';

            return;
        }

        // 500 에러 등 다른 상태 코드도 처리 가능
        if (error.response && error.response.status === 500) {
            console.error('Server error occurred:', error.response.data);
            alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');

            return;
        }

        // 다른 에러는 그대로 처리
        return Promise.reject(error);
    }
);

export default api;
