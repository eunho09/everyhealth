import React, { useEffect, useState } from 'react';
import '../styles/WorkoutLog.css';
import { TiPlus } from "react-icons/ti";
import { FaRegEdit } from "react-icons/fa";
import {routineService} from "../services/routineService";
import AddTodayExercise from "./AddTodayExercise";
import WorkoutCheckbox from "./WorkoutCheckbox";
import {exerciseService} from "../services/exerciseService";
import {todayService} from "../services/todayService";

const WorkoutLog = ({month, year, dateFormat, hasToday, todayData, fetchMonthData, handleIsEditing}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [exercises, setExercises] = useState([]);
    const [routines, setRoutines] = useState([]);
    const [checkList, setCheckList] = useState([]);

    useEffect(() => {
        if (isModalOpen) {
            fetchExercises();
            fetchRoutines();
        }
    }, [isModalOpen]);

    const fetchExercises = async () => {
        try {
            const data = await exerciseService.findExerciseByMemberId();
            setExercises(data);
        } catch (error) {
            console.error(error);
        }
    }

    const fetchRoutines = async () => {
        try {
            const data = await routineService.findRoutineByMemberId();
            setRoutines(data);
        } catch (error) {
            console.error(error);
        }
    }

    const handleModel = (boolean) => {
        setIsModalOpen(boolean);
    }

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


    const handleSaveTodayExercise = async (dateFormat) => {
        try {
            await todayService.addTodayExercise(dateFormat, checkList);
        } catch (error){
            console.error(error);
        }
    }

    return (
        <div className="workout-log">
            <h2>{dateFormat} 운동 일지</h2>

            <button onClick={() => {
                handleModel(true)
            }}>
                <TiPlus/>
            </button>
            <button onClick={() => handleIsEditing(true)}>
                <FaRegEdit/>
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
                    {todayData?.id &&
                        <WorkoutCheckbox
                            fetchMonthData={fetchMonthData}
                            year={year}
                            month={month}
                            todayData={todayData}
                            todayId={todayData.id}
                    />}
                </div>
            ) : (
                <p className="no-log">운동 기록이 없습니다.</p>
            )}
            {isModalOpen && (
                <AddTodayExercise
                    handleModel={handleModel}
                    exercises={exercises}
                    checkList={checkList}
                    setCheckList={setCheckList}
                    checkHandler={checkHandler}
                    routines={routines}
                    handleSaveTodayExercise={handleSaveTodayExercise}
                    month={month}
                    year={year}
                    hasToday={hasToday}
                    dateFormat={dateFormat}
                    fetchMonthData={fetchMonthData}
                />
            )}
        </div>
    );
};

export default WorkoutLog;
