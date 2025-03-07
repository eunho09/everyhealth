import api from "./api";

export const chatService = {
    recentMessage: async (roomId) => {
        const response = await api.get(`/api/rooms/${roomId}/recentMessage?limit=20`)
        return response.data;
    },

    olderMessage: async (roomId, oldestMessageId) => {
        const response = await api.get(`/api/rooms/${roomId}/olderMessage?messageId=${oldestMessageId}`);
        return response.data;
    }
}