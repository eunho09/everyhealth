import React, {useState} from 'react';
import { FiPlusCircle } from "react-icons/fi";
import { FaRegEdit } from "react-icons/fa";
import { FaRegTrashAlt } from "react-icons/fa";
import '../styles/RoutineList.css';
import {routineService} from "../services/routineService";

const RoutineList = ({ routines, open, edit, fetchRoutines}) => {
    const [toggleState, setToggleState] = useState({});

    const toggleExercises = (routineId) => {
        setToggleState((prevState) => ({
            ...prevState,
            [routineId]: !prevState[routineId],
        }));
    };

    const deleteRoutine =  async (id) => {
        try {
            await routineService.deleteRoutineById(id);
            await fetchRoutines();
        } catch (error){
            console.log(error);
        }
    }

    return (
        <ul>
            {routines.map((routine) => (
                <li key={routine.routineId} className="routine-box">
                    <div className="routine-title">
                        <button
                            className="text-button"
                            onClick={() => toggleExercises(routine.routineId)}
                        >
                            {routine.routineName}
                        </button>
                        <div className="button-container">
                            <button onClick={() => open(routine.routineId)}>
                                <FiPlusCircle/>
                            </button>
                            <button onClick={() => edit(routine.routineId)}>
                                <FaRegEdit/>
                            </button>
                            <button onClick={() => deleteRoutine(routine.routineId)}>
                                <FaRegTrashAlt/>
                            </button>
                        </div>
                    </div>
                    {toggleState[routine.routineId] && (
                        <ul className="exercise-lists">
                        {routine.routineExerciseDtoList.length > 0 ? (
                                routine.routineExerciseDtoList.map((exercise, exerciseIndex) => (
                                        <li key={exerciseIndex} className="exercise-box">
                                            <strong>{exercise.exerciseName}</strong>
                                            <ul className="set-list">
                                                {exercise.repWeightList.map((repWeight, index) => (
                                                    <li key={index} className="set-item">
                                                        <div>
                                                            {index + 1}세트
                                                        </div>
                                                        <div>
                                                            {repWeight.reps}회
                                                        </div>
                                                        <div>
                                                            {repWeight.weight !== 0 && `${repWeight.weight}kg`}
                                                        </div>
                                                    </li>
                                                ))}
                                            </ul>
                                        </li>
                                    ))
                            ) : (
                                <li>운동이 없습니다.</li>
                                )}
                        </ul>
                    )}
                </li>
            ))}
        </ul>
    );
};

export default RoutineList;
