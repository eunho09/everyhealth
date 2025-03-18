import React from 'react';
import { FiPlusCircle } from "react-icons/fi";
import { CgArrowsExchange } from "react-icons/cg";
import '../styles/RoutineList.css';

const RoutineList = ({ routines, toggleState, onToggle, open, edit}) => {
    return (
        <ul>
            {routines.map((routine) => (
                <li key={routine.routineId} className="routine-box">
                    <div className="routine-title">
                        <button
                            className="text-button"
                            onClick={() => onToggle(routine.routineId)} // 토글 호출
                        >
                            {routine.routineName}
                        </button>
                        <div>
                            <button className="right-button" onClick={() => open(routine.routineId)}>
                                <FiPlusCircle />
                            </button>
                            <button className="right-button" onClick={() => edit(routine.routineId)}>
                                <CgArrowsExchange />
                            </button>
                        </div>
                    </div>
                    {/* 토글 상태에 따라 운동 목록 표시 */}
                    {toggleState[routine.routineId] && (
                        <ul className="exercise-list">
                            {routine.routineExerciseDtoList.map((exercise, exerciseIndex) => (
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
                            ))}
                        </ul>
                    )}
                </li>
            ))}
        </ul>
    );
};

export default RoutineList;
