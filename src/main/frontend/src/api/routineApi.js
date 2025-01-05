import axios from "axios";
import api from "./api";

export const findRoutineById = async (routineId) => {
    const response = await api.get(`/api/routine/${routineId}`);
    return response.data;
}

export const findRoutineByMemberId = async () => {
    const response = await api.get("/api/member/routines");
    return response.data;
}

export const addRoutineExercise = async (routineId, exerciseInfoList) => {
    const response = await api.post(`/api/routineExercise`, {
        routineId: routineId,
        exerciseInfoList: exerciseInfoList,
    });

    return response.data
};

export const deleteRoutineExercise = async (routineExerciseId) => {
    const response = await api.delete(`/api/routineExercise/${routineExerciseId}`);
    return response.data;
}

export const updateSequence = async (routineId, updateList) => {
    const response = await api.put(`/api/routineExercise/updateSequence/${routineId}`, updateList);
    return response.data;
}

export const updateRoutineExercise = async (routineId, updateList) => {
    const response = await api.patch(`/api/routineExercise/update/${routineId}`, updateList);
    return response.data;
}