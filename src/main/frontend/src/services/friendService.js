import api from "./api";

export const friendService = {
    getRequest: async () => {
        const response = await api.get('/api/member/friend/request');
        return response.data;
    },

    getSuggestedFriends: async () => {
        const response = await api.get('/api/member/suggested-friends');
        return response.data;
    },

    acceptFriend : async (friendId) => {
        const response = await api.post(`/api/friend/accept/${friendId}`);
        return response.data;
    },

    cancelRequest: async (friendId) => {
        const response = await api.post(`/api/friend/cancel/${friendId}`);
        return response.data;
    },

    send: async (friendId) => {
        const response = await api.post(`/api/friend/request/${friendId}`);
        return response.data;
    },

    checkFriendShip: async (friendId) => {
        const response = await api.get(`/api/friend/check/${friendId}`)
        return response.data;
    }
}