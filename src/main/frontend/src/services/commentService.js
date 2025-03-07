import api from "./api";

export const commentService = {
    findByPostId: async (postId) => {
        const response = await api.get(`/api/comment/${postId}`);
        return response.data;
    },

    save: async (postId, text) => {
        const response = await api.post(`/api/comment`, {
            postId:postId,
            text:text
        });
        return response.data;
    }
}