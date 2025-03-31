import React, {useState} from 'react';
import "../styles/AddTodayExercise.css"
import {IoIosArrowBack} from "react-icons/io";
import {todayService} from "../services/todayService";

const AddTodayExercise = ({handleModel, exercises, checkList, setCheckList, checkHandler, routines, handleSaveTodayExercise, dateFormat, month, year, hasToday, fetchMonthData}) => {

    const [activeTab, setActiveTab] = useState("exercises"); // "exercises" 또는 "routines"

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    const save = async () => {
        try {
            await handleSaveToday(dateFormat);
            await handleSaveTodayExercise(dateFormat);
            await fetchMonthData(year, month);
            handleModel(false);
            setCheckList([]);
        } catch (e) {
            console.error(e);
        }
    }

    const handleSaveToday = async (dateFormat) => {
        try {
            if (!hasToday(dateFormat)){
                await todayService.saveToday(dateFormat);

            } else {
                console.log(`${dateFormat} 데이터가 이미 존재합니다.`);
            }

        } catch (e) {
            console.error("데이터 저장 실패:", e);
        }
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
                                    <label htmlFor={`exercise-${exercise.id}`}>{exercise.name}</label>
                                    <input
                                        className="input-routine"
                                        type="checkbox"
                                        id={`exercise-${exercise.id}`}
                                        checked={checkList.some((item) => item.type === "exercise" && item.id === exercise.id)}
                                        onChange={(e) => checkHandler(exercise.id, e.target.checked, "exercise")}
                                    />
                                </div>
                            ))}
                        </div>
                    )}

                    {activeTab === "routines" && Array.isArray(routines) && (
                        <div className="row-column">
                            {routines.map((routine) => (
                                <div key={routine.routineId}>
                                    <label htmlFor={`routine-${routine.routineId}`}>{routine.routineName}</label>
                                    <input
                                        className="input-routine"
                                        type="checkbox"
                                        id={`routine-${routine.routineId}`}
                                        checked={checkList.some((item) => item.type === "routine" && item.id === routine.routineId)}
                                        onChange={(e) => checkHandler(routine.routineId, e.target.checked, "routine")}
                                    />
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <button onClick={() => save()}>저장
                </button>
            </div>
        </div>
    );
};

export default AddTodayExercise;