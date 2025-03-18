import api from "./api";

export const todayService = {
    findOneTodayById: async (todayId) => {
        const response = await api.get(`/api/today/${todayId}`);
        return response.data;
    },

    getTodayByMonth: async (month) => {
        const response = await api.get(`/api/today/month/${month}`)
        return response.data;
    },

    getTodayDate: async (date) => {
        const response = await api.get(`/api/today/date/${date}`);
        return response.data;
    },

    getTodayDateByFriend: async (friendId, date) => {
        const response = await api.get(`/api/today/friendAndDate/${friendId}/${date}`);
        return response.data;
    },

    saveToday: async () => {
        const response = await api.post("/api/today");
        return response.data;
    },

    addTodayExercise: async (date, checkList) => {
        const response = await api.post(`/api/today/addTodayExercise/${date}`, checkList);
        return response.data;
    },

    deleteTodayExercise: async (todayExerciseId) => {
        const response = await api.delete(`/api/delete/todayExercise/${todayExerciseId}`);
        return response.data;
    },

    updateSequence: async (todayId, updateList) => {
        const response = await api.patch(`/api/todayExercise/updateSequence/${todayId}`, updateList);
        return response.data;
    },

    updateTodayExercise: async (todayId, updateList) => {
        const response = await api.patch(`/api/update/todayExercise/${todayId}`, updateList);
        return response.data;
    },

    updateCheckbox: async (todayId, check) => {
        const response = await api.post(`/api/today/checkbox/${todayId}?checked=${check}`);
        return response.data;
    },

    findByFriendAndMonth: async (friendId, month) => {
        const response = await api.get(`/api/today/friendAndMonth/${friendId}/${month}`);
        return response.data;
    }
}