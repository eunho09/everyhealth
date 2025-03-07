import api from "./api";


export const loginService = {
    logout: async () => {
        const response = await api.get("/api/logout");
        return response.data;
    },

    loginCheck: async () => {
        const response = await api.get("/api/login/check");
        return response.data;
    }
}