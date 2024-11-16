import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RoutineList from './RoutineList';
import AddRoutineExercise from './AddRoutineExercise';

const RoutineManager = () => {
    const [routines, setRoutines] = useState([]);
    const [exercises, setExercises] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRoutineId, setSelectedRoutineId] = useState(null);
    const [checkedList, setCheckedList] = useState([]);

    // 루틴 데이터 로드
    useEffect(() => {
        axios.get("/api/routine/1")
            .then((response) => setRoutines(response.data))
            .catch((error) => console.error(error));
    }, []);

    // 운동 데이터 로드
    useEffect(() => {
        axios.get("/api/exercises")
            .then((response) => setExercises(response.data))
            .catch((error) => console.error(error));
    }, []);

    const openModal = (routineId) => {
        setSelectedRoutineId(routineId);
        setIsModalOpen(true);
        setCheckedList([]);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setCheckedList([]);
    };

    const saveExercises = () => {
        axios.post(`/api/routine/${selectedRoutineId}/exercises`, { exercises: checkedList })
            .then(() => axios.get("/api/routine/1"))
            .then((response) => {
                setRoutines(response.data);
                closeModal();
            })
            .catch((error) => console.error(error));
    };

    return (
        <div>
            <h2>운동 목록</h2>
            <RoutineList
                routines={routines}
                onAddExercise={openModal}
            />
            {isModalOpen && (
                <AddRoutineExercise
                    exercises={exercises}
                    checkedList={checkedList}
                    setCheckedList={setCheckedList}
                    onClose={closeModal}
                    onSave={saveExercises}
                />
            )}
        </div>
    );
};

export default RoutineManager;
