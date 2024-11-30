import axios from "axios";

export const findRoutineById = async (routineId) => {
    const response = await axios.get(`/api/routine/${routineId}`);
    return response.data;
}

export const findRoutineByMemberId = async () => {
    const response = await axios.get("/api/member/1/routines");
    return response.data;
}

export const addRoutineExercise = async (routineId, exerciseInfoList) => {
    const response = await axios.post(`/api/routineExercise`, {
        routineId: routineId,
        exerciseInfoList: exerciseInfoList,
    });

    return response.data
};

export const deleteRoutineExercise = async (routineExerciseId) => {
    const response = await axios.delete(`/api/routineExercise/${routineExerciseId}`);
    return response.data;
}

export const updateSequence = async (routineId, updateList) => {
    const response = await axios.put(`/api/routineExercise/updateSequence/${routineId}`, updateList);
    return response.data;
}

export const updateRoutineExercise = async (routineId, updateList) => {
    const response = await axios.patch(`/api/routineExercise/update/${routineId}`, updateList);
    return response.data;
}