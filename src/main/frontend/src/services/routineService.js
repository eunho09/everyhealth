import api from "./api";

export const routineService = {
    save: async (name) => {
        const response = await api.post(`/api/routine?name=${name}`)
        return response.data;
    },

    findRoutineById: async (routineId) => {
        const response = await api.get(`/api/routine/${routineId}`);
        return response.data;
    },

    findRoutineByMemberId: async () => {
        const response = await api.get("/api/member/routines");
        return response.data;
    },

    addRoutineExercise: async (routineId, exerciseInfoList) => {
        const response = await api.post(`/api/routineExercise`, {
            routineId: routineId,
            exerciseInfoList: exerciseInfoList,
        });

        return response.data
    },

    deleteRoutineExercise: async (routineExerciseId) => {
        const response = await api.delete(`/api/routineExercise/${routineExerciseId}`);
        return response.data;
    },

    updateSequence: async (routineId, updateList) => {
        const response = await api.patch(`/api/routineExercise/updateSequence/${routineId}`, updateList);
        return response.data;
    },

    updateRoutineExercise: async (routineId, updateList) => {
        const response = await api.patch(`/api/routineExercise/update/${routineId}`, updateList);
        return response.data;
    },

    deleteRoutineById: async (id) => {
        const response = await api.delete(`/api/routine/${id}`);
        return response.data;
    }
}