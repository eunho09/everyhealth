import axios from "axios";
import api from "./api";


export const findExerciseByMemberId = async () => {
    const response = await api.get("/api/member/1/exercises");
    return response.data;
}

export const saveExercise = async (exerciseName, note, repWeight, classification) => {
    const response = await api.post("/api/exercise", {
        name: exerciseName,
        memberId: 1,
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
    }, {
        headers: {
            'authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7Yys7LyA7J207YGsIiwiaWQiOjIsImF1dGhvcml0aWVzIjoiVVNFUiIsImlhdCI6MTczMzIwNzQwMywiZXhwIjoxNzMzMjkzODAzfQ.p_ca5WENqTQuRfTUcdGvX8TMyh_zatA8B3rBwxE_ep6JZPbcOuQOC84gp4NB3Jg1pFWMn4OdUg29LfBIdW-kzg',
        }
    });


    return response.data;
}

export const getClassification = async () => {
    const response = await api.get("/api/enum/classification")
    return response.data;
}