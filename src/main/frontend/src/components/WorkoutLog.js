import React, { useEffect, useState } from 'react';
import './WorkoutLog.css';
import { TiPlus } from "react-icons/ti";
import {IoIosArrowBack} from "react-icons/io";
import axios from "axios";
import {CgArrowsExchange} from "react-icons/cg";

const WorkoutLog = ({ date, todayData, handleSaveToday, fetchMonthData, handleIsEditing}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [exercises, setExercises] = useState([]);
    const [activeTab, setActiveTab] = useState("exercises"); // "exercises" 또는 "routines"
    const [routines, setRoutines] = useState([]);

    const [checkList, setCheckList] = useState([]);

    const formatter = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Seoul' }); // 한국 시간으로 포멧팅
    const formattedDate = formatter.format(date); // YYYY-MM-DD 형식 반환

    const month = date.getMonth() + 1;

    const fetchExercises = async () => {
        try {
            const response = await axios.get("/api/member/1/exercises");
            console.log(response.data)
            setExercises(response.data);
        } catch (error) {
            console.error(error);
        }
    }

    const fetchRoutines = async () => {
        try {
            const response = await axios.get("/api/member/1/routines");
            console.log(response.data)
            setRoutines(response.data);
        } catch (error) {
            console.error(error);
        }
    }

    const handleModel = (boolean) => {
        setIsModalOpen(boolean);
    }

    useEffect(() => {
        console.log(isModalOpen);
    }, [isModalOpen]);

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    const checkHandler = (value, isChecked, field) => {

        if (isChecked) {
            setCheckList((prev) => [...prev, {id:value, type:field, sequence:checkList.length + 1}])
        } else {
            setCheckList((prev) =>
                prev
                    .filter((item) => !(item.id === value && item.type === field))
                    .map((item, index) => ({...item, sequence: index + 1}))
            );
        }
    }

    useEffect(() => {
        console.log(checkList)
    }, [checkList]);

    const handleSaveTodayExercise = async (formattedDate) => {
        try {
            const response = await axios.post(`/api/today/addTodayExercise/${formattedDate}`, checkList);
            console.log(response.data);
        } catch (error){
            console.error(error);
        }
    }

    useEffect(() => {
        if (isModalOpen) {
            fetchExercises();
            fetchRoutines();
        }
    }, [isModalOpen]); // isModalOpen이 true로 변경될 때 데이터를 로드

    return (
        <div className="workout-log">
            <h2>{formattedDate} 운동 일지</h2>
            <button onClick={() => {
                handleSaveToday(formattedDate);
                handleModel(true)
            }}>
                <TiPlus />
            </button>
            <button onClick={() => handleIsEditing(true)}>
                <CgArrowsExchange />
            </button>
            {todayData ? (
                <div>
                    <ul>
                        {todayData.todayExercises.map((todayExercise, index) => (
                            <li key={index}>
                                <div>{todayExercise.exerciseName}</div>
                                <ul className="set-list">
                                    {todayExercise.repWeight.map((set, setIndex) => (
                                        <li key={setIndex}>
                                            {setIndex + 1}세트: {set[0]}회 {set[1] !== 0 && `${set[1]}kg`}
                                        </li>
                                    ))}
                                </ul>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <p className="no-log">운동 기록이 없습니다.</p>
            )}
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <div className="button-position">
                            <button
                                className="back-button rotate-right"
                                onClick={() => handleModel(false)}
                            >
                                <IoIosArrowBack />
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
                                                onChange={(e) => checkHandler(routine.routineId, e.target.checked,"routine")}
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
                        }}>저장</button>
                    </div>
                </div>

            )}
        </div>
    );
};

export default WorkoutLog;
