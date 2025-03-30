import React, {useEffect, useState} from 'react';
import { IoIosArrowBack } from "react-icons/io";
import "../styles/Modal.css";
import {exerciseService} from "../services/exerciseService";
import {routineService} from "../services/routineService";

const AddRoutineExercise = ({ routines, routineId, fetchRoutines, onClose}) => {

    const [exercises, setExercises] = useState([]);
    const [checkedList, setCheckedList] = useState([]);
    const [sequence, setSequence] = useState(1);

    useEffect(() => {
        const fetchExerciseByMember = async () => {
            try {
                const data = await exerciseService.findExerciseByMemberId();
                setExercises(data);
            } catch (error){
                console.error(error);
                throw error;
            }
        }

        fetchExerciseByMember();
        initSequence(routineId);
    }, [])

    const checkHandler = async (exerciseId, isChecked) => {
        if (isChecked) {
            setCheckedList((prev) => [
                ...prev,
                { exerciseId: exerciseId, sequence: sequence },
            ]);
            setSequence((prev) => prev + 1);
        } else {
            const newSequence = lastSequence(routineId);
            setCheckedList((prev) =>
                prev
                    .filter((item) => item.exerciseId !== exerciseId)
                    .map((item, index) => ({
                        ...item,
                        sequence: newSequence + index,
                    }))
            );
            setSequence((prev) => prev - 1);
        }
    };

    const getSequence = (routineId) => {
        console.log("routineId : " + routineId)
        const routine = routines.find((r) => r.routineId === routineId);
        const lastSequence = routine.routineExerciseDtoList.length > 0
            ? Math.max(...routine.routineExerciseDtoList.map((e) => e.sequence))
            : 0;

        return lastSequence;
    }

    const initSequence = (routineId) => {
        const findSequence = getSequence(routineId);

        setSequence(findSequence + 1);
    }

    const lastSequence = (routineId) => {
        const findSequence = getSequence(routineId);
        return findSequence + 1
    };


    const addRoutineExercises = async (routineId) => {
        try {
            await routineService.addRoutineExercise(routineId, checkedList);
            await fetchRoutines();
            onClose();
        } catch (error) {
            console.error(error);
        }

    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="button-position">
                    <button onClick={() => onClose(routineId)} className="back-button rotate-right"><IoIosArrowBack /></button>
                </div>
                <h3>운동 추가</h3>
                <div className="row-column">
                    {exercises.map((exercise) => (
                        <div key={exercise.id}>
                            <input
                                className="input-routine"
                                type="checkbox"
                                id={`exercise-${exercise.id}`}
                                onChange={(e) => checkHandler(exercise.id, e.target.checked)}
                            />
                            <label htmlFor={`exercise-${exercise.id}`}>{exercise.name}</label>
                        </div>
                    ))}
                </div>
                <button onClick={() => addRoutineExercises(routineId)}>저장</button>
            </div>
        </div>
    );
};

export default AddRoutineExercise;
