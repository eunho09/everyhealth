import api from "./api";

export const memberService = {
    findPost: async () => {
        const response = await api.get('/api/member/posts');
        return response.data;
    },

    findFriend: async () => {
        const response = await api.get('api/member/friend');
        return response.data;
    },

    findFriendById: async (friendId) => {
        const response = await api.get(`/api/member/friend/${friendId}`);
        return response.data;
    },
}