import api from "./api";

export const clubService = {
    findClub: async (roomId) => {
        const response = await api.get(`/api/club/chatRoom/${roomId}`);
        return response.data;
    },

    leaveChatRoom: async (clubId) => {
        const response = await api.delete(`/api/club/${clubId}/leave`);
        return response.data;
    },

    save: async (formData) => {
        const response = await api.post("/api/club", {
            title: formData.title,
            content: formData.content,
            location: formData.location,
            schedule: formData.schedule,
            highlights: formData.highlights
        });

        return response.data;
    },

    findAll: async () => {
        const response = await api.get("/api/clubs");
        return response.data;
    },

    findByNameAndMyClub: async (searchClubName, isMyClubs) => {
        const response = await api.get(`/api/clubs?name=${searchClubName}&isMyClubs=${isMyClubs}`)
        return response.data;
    },

    join: async (clubId) => {
        const response = await api.post(`/api/club/${clubId}/join`);
        return response.data;
    }
}