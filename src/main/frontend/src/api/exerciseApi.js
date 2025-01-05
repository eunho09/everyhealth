import axios from "axios";
import api from "./api";


export const findExerciseByMemberId = async () => {
    const response = await api.get("/api/member/exercises");
    return response.data;
}

export const saveExercise = async (exerciseName, note, repWeight, classification) => {
    const response = await api.post("/api/exercise", {
        name: exerciseName,
        memo: note,
        repWeight: repWeight,
        classification: classification,
    });

    return response.data;
}

export const updateExercise = async (exerciseId, selectedExercise) => {
    const response = await api.patch(`/api/exercise/${exerciseId}`, {
        name: selectedExercise.name,
        memo: selectedExercise.memo,
        classification: selectedExercise.classification,
        repWeight: selectedExercise.repWeight,
    });

    return response.data;
}

export const getClassification = async () => {
    const response = await api.get("/api/enum/classification")
    return response.data;
}