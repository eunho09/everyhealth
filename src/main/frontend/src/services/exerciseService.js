import api from "./api";


export const exerciseService = {
    findExerciseByMemberId: async () => {
        const response = await api.get("/api/member/exercises");
        return response.data;
    },

    saveExercise: async (exerciseName, note, repWeight, classification) => {
        const response = await api.post("/api/exercise", {
            name: exerciseName,
            memo: note,
            repWeightList: repWeight,
            classification: classification,
        });

        return response.data;
    },

    updateExercise: async (exerciseId, selectedExercise) => {
        const response = await api.patch(`/api/exercise/${exerciseId}`, {
            name: selectedExercise.name,
            memo: selectedExercise.memo,
            classification: selectedExercise.classification,
            repWeightList: selectedExercise.repWeightList,
        });

        return response.data;
    },

    getClassification: async () => {
        const response = await api.get("/api/enum/classification")
        return response.data;
    }
}