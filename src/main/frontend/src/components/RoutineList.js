import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './RoutineList.css';
import Plus from "./Plus";
import {FiPlusCircle} from "react-icons/fi";

const RoutineList = () => {
    const [routines, setRoutines] = useState([]);
    const [toggleState, setToggleState] = useState({});

    useEffect(() => {
        axios.get("/api/routine/1")
            .then((response) => setRoutines(response.data))
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        console.log(routines);
    }, [routines])

    const toggleExercises = (routineId) => {
        setToggleState(prevState => ({
            ...prevState,
            [routineId]: !prevState[routineId]
        }));
    };

    return (
        <>
            <h2>운동 목록</h2>
            <ul>
                {routines.map((routine) => (
                    <li key={routine.routineId} className="routine-box">
                        <div className="routine-title">
                            <button className="text-button" onClick={() => toggleExercises(routine.routineId)}>
                                {routine.routineName}
                            </button>
                            <button className="right-button">
                                <FiPlusCircle />
                            </button>
                        </div>
                        {toggleState[routine.routineId] && (
                            <ul className="exercise-list">
                                {routine.routineExerciseDtoList.map((exercise, exerciseIndex) => (
                                    <li key={`exercise-${routine.routineId}-${exerciseIndex}`} className="exercise-box">
                                        <strong>{exercise.exerciseName}</strong>
                                        <ul className="set-list">
                                            {exercise.repWeight.map((set, setIndex) => (
                                                <li key={`set-${routine.routineId}-${exerciseIndex}-${setIndex}`} className="set-item">
                                                    <div>
                                                        {setIndex + 1}세트
                                                    </div>
                                                    <div>
                                                        {set[0]}회
                                                    </div>
                                                    {set[1] !== 0 && (
                                                        <div>
                                                            {set[1]}kg
                                                        </div>
                                                    )}
                                                </li>
                                            ))}
                                        </ul>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </li>
                ))}
            </ul>
        </>
    );
};

export default RoutineList;