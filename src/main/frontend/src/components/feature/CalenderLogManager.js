import React, {useEffect, useState} from 'react';
import WorkoutCalendar from "../WorkoutCalendar";
import WorkoutLog from "../WorkoutLog";
import axios from "axios";
import moment from "moment";

const CalenderLogManager = () => {

    const [date, setDate] = useState(new Date());
    const [monthData, setMonthData] = useState([]);
    const [todayData, setTodayData] = useState();


    useEffect(() => {
        //0부터 시작
        const month = date.getMonth() + 1;
        fetchMonthData(month);

    }, [])

    const fetchMonthData = async (month) => {
        try {
            const response = await axios.get(`/api/today/month/${month}`);
            console.log(response.data);
            const getLocalDate = response.data.map(value => value.localDate);

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
                    const response = await axios.get(`/api/today/date/${dateFormat}`);
                    console.log(response.data);
                    setTodayData(response.data)
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
            await axios.post("/api/today", {
                localDate: dateFormat
            }).then(response => console.log(response.data));
        }
        const month = date.getMonth() + 1;
        fetchMonthData(month);
    }

    const hasToday = (dateFormat) => {
        return monthData.includes(dateFormat);
    }




    return (
        <div className="container">
            <WorkoutCalendar date={date} handleDateChange={handleDateChange} workoutDates={monthData}/>
            <WorkoutLog date={date} todayData={todayData} handleSaveToday={handleSaveToday} fetchMonthData={fetchMonthData}/>
        </div>
    );
};

export default CalenderLogManager;