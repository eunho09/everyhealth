import React, {useEffect, useState} from 'react';
import WorkoutCalendar from "../WorkoutCalendar";
import WorkoutLog from "../WorkoutLog";
import axios from "axios";
import moment from "moment";
import UpdateToday from "../UpdateToday";
import {getTodayByMonth, getTodayDate, saveToday} from "../../api/todayApi";

const CalenderLogManager = () => {

    const [date, setDate] = useState(new Date());
    const [monthData, setMonthData] = useState([]);
    const [todayData, setTodayData] = useState();
    const [isEditing, setIsEditing] = useState(false);
    const [selectTodayId, setSelectTodayId] = useState();


    useEffect(() => {
        //0부터 시작
        const month = date.getMonth() + 1;
        fetchMonthData(month);

    }, [])

    const fetchMonthData = async (month) => {
        try {
            const data = await getTodayByMonth(month);
            const getLocalDate = data.map(value => value.localDate);

            setMonthData(getLocalDate);
        } catch (e) {
            console.error(e);
        }
    }


    useEffect(() => {
        const dateFormat = moment(date).format("YYYY-MM-DD");

        if (hasToday(dateFormat)){
            const fetchDateData = async () => {
                try {
                    const data = await getTodayDate(dateFormat);
                    setTodayData(data)
                    setSelectTodayId(data.id);
                } catch (e) {
                    console.error(e);
                }
            }

            fetchDateData();
        } else {
            setTodayData(undefined);
        }

    }, [date])

    const handleDateChange = (newDate) => {
        setDate(newDate);
    };

    const handleSaveToday = async (dateFormat) => {
        if (!hasToday(dateFormat)){
            await saveToday(dateFormat);
        }
        const month = date.getMonth() + 1;
        fetchMonthData(month);
    }

    const hasToday = (dateFormat) => {
        return monthData.includes(dateFormat);
    }

    const handleIsEditing = (boolean) => {
        setIsEditing(boolean);
    }



    return (
        <div className="flex">
            <WorkoutCalendar date={date} handleDateChange={handleDateChange} workoutDates={monthData}/>
            <WorkoutLog date={date} todayData={todayData} handleSaveToday={handleSaveToday} fetchMonthData={fetchMonthData} handleIsEditing={handleIsEditing}/>
            {isEditing && (
                <UpdateToday todayId={selectTodayId} handleIsEditing={handleIsEditing}/>
            )}
        </div>
    );
};

export default CalenderLogManager;