import axios from 'axios';

export const findOneTodayById = async (todayId) => {
    axios.defaults.withCredentials = true;
    const response = await axios.get(`/api/today/${todayId}`);
    return response.data;
}

export const getTodayByMonth = async (month) => {
    axios.defaults.withCredentials = true;
    const response = await axios.get(`/api/today/month/${month}`, {
        headers : {
            'authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7Yys7LyA7J207YGsIiwiaWQiOjIsImF1dGhvcml0aWVzIjoiVVNFUiIsImlhdCI6MTczMzIwNzQwMywiZXhwIjoxNzMzMjkzODAzfQ.p_ca5WENqTQuRfTUcdGvX8TMyh_zatA8B3rBwxE_ep6JZPbcOuQOC84gp4NB3Jg1pFWMn4OdUg29LfBIdW-kzg',
        }
    });
    return response.data;
}

export const getTodayDate = async (date) => {
    const response = await axios.get(`/api/today/date/${date}`);
    return response.data;
}

export const saveToday = async (date) => {
    const response = await axios.post("/api/today", {
        localDate: date
    })

    return response.data;
}

export const addTodayExercise = async (date, checkList) => {
    const response = await axios.post(`/api/today/addTodayExercise/${date}`, checkList);
    return response.data;
}

export const deleteTodayExercise = async (todayExerciseId) => {
    const response = await axios.delete(`/api/todayExercise/${todayExerciseId}`);
    return response.data;
}

export const updateSequence = async (todayId, updateList) => {
    const response = await axios.patch(`/api/todayExercise/updateSequence/${todayId}`, updateList);
    return response.data;
}

export const updateTodayExercise = async (todayId, updateList) => {
    const response = await axios.patch(`/api/update/todayExercise/${todayId}`, updateList);
    return response.data;
}