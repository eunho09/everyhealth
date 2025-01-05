import api from "./api";


export const logout = async () => {
    const response = await api.get("/api/logout");
    return response.data;
}

export const loginCheck = async () => {
    const response = await api.get("/api/login/check");
    return response.data;
}

