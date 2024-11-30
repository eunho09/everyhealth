import React, {useState} from 'react';
import {IoIosArrowBack} from "react-icons/io";

const AddTodayExercise = ({handleModel, exercises, checkList, checkHandler, routines, handleSaveTodayExercise, formattedDate, month, fetchMonthData}) => {

    const [activeTab, setActiveTab] = useState("exercises"); // "exercises" 또는 "routines"

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="button-position">
                    <button
                        className="back-button rotate-right"
                        onClick={() => handleModel(false)}
                    >
                        <IoIosArrowBack/>
                    </button>
                </div>
                <h2>추가</h2>

                {/* Tab Navigation */}
                <div className="tab-navigation">
                    <button
                        className={activeTab === "exercises" ? "active-tab" : ""}
                        onClick={() => handleTabChange("exercises")}
                    >
                        운동
                    </button>
                    <button
                        className={activeTab === "routines" ? "active-tab" : ""}
                        onClick={() => handleTabChange("routines")}
                    >
                        루틴
                    </button>
                </div>

                {/* Tab Content */}
                <div className="tab-content">
                    {activeTab === "exercises" && (
                        <div className="row-column">
                            {exercises.map((exercise) => (
                                <div key={exercise.id}>
                                    <input
                                        className="input-routine"
                                        type="checkbox"
                                        id={`exercise-${exercise.id}`}
                                        checked={checkList.some((item) => item.type === "exercise" && item.id === exercise.id)}
                                        onChange={(e) => checkHandler(exercise.id, e.target.checked, "exercise")}
                                    />
                                    <label htmlFor={`exercise-${exercise.id}`}>{exercise.name}</label>
                                </div>
                            ))}
                        </div>
                    )}

                    {activeTab === "routines" && Array.isArray(routines) && (
                        <div className="row-column">
                            {routines.map((routine) => (
                                <div key={routine.routineId}>
                                    <input
                                        className="input-routine"
                                        type="checkbox"
                                        id={`routine-${routine.routineId}`}
                                        checked={checkList.some((item) => item.type === "routine" && item.id === routine.routineId)}
                                        onChange={(e) => checkHandler(routine.routineId, e.target.checked, "routine")}
                                    />
                                    <label htmlFor={`routine-${routine.routineId}`}>{routine.routineName}</label>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <button onClick={() => {
                    handleSaveTodayExercise(formattedDate);
                    fetchMonthData(month)
                }}>저장
                </button>
            </div>
        </div>
    );
};

export default AddTodayExercise;