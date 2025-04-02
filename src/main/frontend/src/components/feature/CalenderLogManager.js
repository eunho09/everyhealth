import React, {useEffect, useState} from 'react';
import WorkoutCalendar from "../WorkoutCalendar";
import WorkoutLog from "../WorkoutLog";
import moment from "moment";
import UpdateToday from "../UpdateToday";
import {todayService} from "../../services/todayService";

const CalenderLogManager = () => {
    const [date, setDate] = useState(new Date());
    const [month, setMonth] = useState(date.getMonth() + 1);
    const [year, setYear] = useState(date.getFullYear());
    const [dateFormat, setDateFormat] = useState(moment(date).format("YYYY-MM-DD"));
    const [monthData, setMonthData] = useState([]);
    const [todayData, setTodayData] = useState();
    const [isEditing, setIsEditing] = useState(false);
    const [selectTodayId, setSelectTodayId] = useState();

    useEffect(() => {
        if (!!year && !!month) {
            fetchMonthData(year, month);
        }
    }, [year, month]);

    useEffect(() => {
        if (hasToday(dateFormat)){
            const fetchTodayDateByLocalDate = async () => {
                try {
                    const data = await todayService.getTodayDate(dateFormat);
                    setSelectTodayId(data.id);
                    setTodayData(data);
                } catch (e) {
                    console.error("날짜 데이터 가져오기 실패:", e);
                    setSelectTodayId(undefined);
                    setTodayData(undefined);
                }
            };

            fetchTodayDateByLocalDate();
        } else {
            setTodayData(undefined);
            setSelectTodayId(undefined);
        }
    }, [date, monthData]);

    const fetchMonthData = async (year, month) => {
        try {
            const data = await todayService.getTodayByMonth(year, month);
            setMonthData(data);
        } catch (e) {
            console.error("월 데이터 가져오기 실패:", e);
        }
    };

    const handleDateChange = (newDate) => {
        setDate(newDate);
        setMonth(newDate.getMonth() + 1);
        setYear(newDate.getFullYear());
        setDateFormat(moment(newDate).format("YYYY-MM-DD"));

        setIsEditing(false);
    };

    const hasToday = (dateFormat) => {
        return monthData.find(data => data.localDate === dateFormat);
    };

    const handleIsEditing = (boolean) => {
        setIsEditing(boolean);

        if (!boolean && !hasToday(dateFormat)) {
            setSelectTodayId(undefined);
        }
    };

    return (
        <div className="flex">
            <WorkoutCalendar
                date={date}
                handleDateChange={handleDateChange}
                workoutDates={monthData}
                fetchMonthData={fetchMonthData}
            />
            <WorkoutLog
                year={year}
                month={month}
                dateFormat={dateFormat}
                hasToday={hasToday}
                todayData={todayData}
                fetchMonthData={fetchMonthData}
                handleIsEditing={handleIsEditing}
            />
            {isEditing && selectTodayId && (
                <UpdateToday
                    hasToday={hasToday}
                    setTodayData={setTodayData}
                    dateFormat={dateFormat}
                    todayId={selectTodayId}
                    handleIsEditing={handleIsEditing}
                />
            )}
        </div>
    );
};

export default CalenderLogManager;