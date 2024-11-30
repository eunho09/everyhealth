import axios from "axios";

export const findExerciseByMemberId = async () => {
    const response = await axios.get("/api/member/1/exercises");
    return response.data;
}

export const saveExercise = async (exerciseName, note, repWeight, classification) => {
    const response = await axios.post("/api/exercise", {
        name: exerciseName,
        memberId: 1,
        memo: note,
        repWeight: repWeight,
        classification: classification,
    });

    return response.data;
}

export const updateExercise = async (exerciseId, selectedExercise) => {
    const response = await axios.patch(`/api/exercise/${exerciseId}`, {
        name: selectedExercise.name,
        memo: selectedExercise.memo,
        classification: selectedExercise.classification,
        repWeight: selectedExercise.repWeight,
    })

    return response.data;
}

export const getClassification = async () => {
    const response = await axios.get("/api/enum/classification")
    return response.data;
}