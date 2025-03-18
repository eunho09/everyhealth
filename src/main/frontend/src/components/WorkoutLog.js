import React, { useEffect, useState } from 'react';
import '../styles/WorkoutLog.css';
import { TiPlus } from "react-icons/ti";
import {IoIosArrowBack} from "react-icons/io";
import axios from "axios";
import {CgArrowsExchange} from "react-icons/cg";
import {routineService} from "../services/routineService";
import AddTodayExercise from "./AddTodayExercise";
import WorkoutCheckbox from "./WorkoutCheckbox";
import {exerciseService} from "../services/exerciseService";
import {todayService} from "../services/todayService";

const WorkoutLog = ({ onDataChanged, date, todayData, handleSaveToday, fetchMonthData, handleIsEditing}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [exercises, setExercises] = useState([]);
    const [routines, setRoutines] = useState([]);
    const [checkList, setCheckList] = useState([]);

    const formatter = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Seoul' }); // 한국 시간으로 포멧팅
    const formattedDate = formatter.format(date); // YYYY-MM-DD 형식 반환

    const month = date.getMonth() + 1;

    const fetchExercises = async () => {
        try {
            const data = await exerciseService.findExerciseByMemberId();
            console.log(data)
            setExercises(data);
        } catch (error) {
            console.error(error);
        }
    }

    const fetchRoutines = async () => {
        try {
            const data = await routineService.findRoutineByMemberId();
            console.log(data)
            setRoutines(data);
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
            const data = todayService.addTodayExercise(formattedDate, checkList);

            console.log(data);
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
            {todayData?.todayExercises?.length > 0 ? (
                <div>
                    <ul>
                        {todayData.todayExercises.map((todayExercise, index) => (
                            <li key={index}>
                                <div>{todayExercise.exerciseName}</div>
                                <ul className="set-list">
                                    {todayExercise.repWeightList.map((repWeight, setIndex) => (
                                        <li key={setIndex}>
                                            {setIndex + 1}세트: {repWeight.reps}회 {repWeight.weight !== 0 && `${repWeight.weight}kg`}
                                        </li>
                                    ))}
                                </ul>
                            </li>
                        ))}
                    </ul>
                    {todayData?.id && <WorkoutCheckbox onDataChanged={onDataChanged} todayId={todayData.id} />}
                </div>
            ) : (
                <p className="no-log">운동 기록이 없습니다.</p>
            )}
            {isModalOpen && (
                <AddTodayExercise
                    onDataChanged={onDataChanged}
                    handleModel={handleModel}
                    exercises={exercises}
                    checkList={checkList}
                    setCheckList={setCheckList}
                    checkHandler={checkHandler}
                    routines={routines}
                    handleSaveToday={handleSaveToday}
                    handleSaveTodayExercise={handleSaveTodayExercise}
                    formattedDate={formattedDate}
                    month={month}
                    fetchMonthData={fetchMonthData}
                />
            )}
        </div>
    );
};

export default WorkoutLog;
