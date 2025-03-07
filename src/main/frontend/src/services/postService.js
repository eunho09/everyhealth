import api from "./api";

export const postService = {
    findAll: async (limit) => {
        const response = await api.get("/api/posts", {
            limit: limit
        });
        return response.data;
    },

    scroll: async (limit, postId) => {
        const response = await api.get("/api/posts/scroll", {
            limit:limit,
            postId:postId
        });
        return response.data;
    },

    getPostImageUrl: async (imageUrl) => {
        const response = await api.get(`/api/images/${imageUrl}`, {
            responseType: "blob"
        });
        return response.data;
    },

    save: async (formData) => {
        const response = await api.post("/api/post", formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        });
        return response.data;
    },

    findFriendPosts: async (friendId) => {
        const response = await api.get(`/api/posts/friend/${friendId}`);
        return response.data;
    }
}