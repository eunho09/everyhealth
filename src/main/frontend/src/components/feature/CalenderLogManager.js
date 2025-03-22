import React, {useCallback, useEffect, useState} from 'react';
import WorkoutCalendar from "../WorkoutCalendar";
import WorkoutLog from "../WorkoutLog";
import moment from "moment";
import UpdateToday from "../UpdateToday";
import {todayService} from "../../services/todayService";

const CalenderLogManager = () => {
    const [date, setDate] = useState(new Date());
    const [monthData, setMonthData] = useState([]);
    const [todayData, setTodayData] = useState();
    const [isEditing, setIsEditing] = useState(false);
    const [selectTodayId, setSelectTodayId] = useState();

    // fetchMonthData를 useCallback으로 메모이제이션
    const fetchMonthData = useCallback(async (year, month) => {
        try {
            // 인자로 month를 받지 않으면 현재 date 객체의 월을 사용
            const targetMonth = month || date.getMonth() + 1;
            const targetYear = year || date.getFullYear();
            console.log(`${targetMonth}월 데이터를 가져옵니다.`);
            const data = await todayService.getTodayByMonth(targetYear, targetMonth);
            setMonthData(data);
        } catch (e) {
            console.error("월 데이터 가져오기 실패:", e);
        }
    }, [date]); // date가 변경될 때 함수 재생성

    // 데이터 변경 이벤트 핸들러 - 하위 컴포넌트에서 호출할 함수
    const handleEventChangeData = useCallback(() => {
        console.log("이벤트 발생, 캘린더를 새로고침합니다.");
        fetchMonthData(); // 현재 date 객체의 월을 사용
    }, [fetchMonthData]); // fetchMonthData가 변경될 때 함수 재생성

    // 컴포넌트 마운트 시 초기 데이터 로드
    useEffect(() => {
        const month = date.getMonth() + 1;
        const year = date.getFullYear();
        fetchMonthData(year, month);
    }, [fetchMonthData]); // fetchMonthData가 변경될 때 실행

    // 날짜 변경 시 해당 날짜의 데이터 로드
    useEffect(() => {
        const dateFormat = moment(date).format("YYYY-MM-DD");
        if (hasToday(dateFormat)){
            const fetchDateData = async () => {
                try {
                    const data = await todayService.getTodayDate(dateFormat);
                    setSelectTodayId(data.id);
                    setTodayData(data);
                } catch (e) {
                    console.error("날짜 데이터 가져오기 실패:", e);
                }
            };
            fetchDateData();
        } else {
            setTodayData(undefined);
        }
    }, [date, monthData]); // monthData가 변경되었을 때도 실행되도록 수정

    useEffect(() => {
        console.log(todayData);
    }, [todayData])

    const handleDateChange = (newDate) => {
        setDate(newDate);
    };

    const handleSaveToday = async (dateFormat) => {
        try {
            if (!hasToday(dateFormat)){
                await todayService.saveToday(dateFormat);
                console.log(`${dateFormat} 데이터 저장 성공`);
            } else {
                console.log(`${dateFormat} 데이터가 이미 존재합니다.`);
            }
            // 저장 후 월 데이터 새로고침
            handleEventChangeData();
        } catch (e) {
            console.error("데이터 저장 실패:", e);
        }
    };

    const hasToday = (dateFormat) => {
        return monthData.find(data => data.localDate === dateFormat);
    };

    const handleIsEditing = (boolean) => {
        setIsEditing(boolean);
    };

    return (
        <div className="flex">
            <WorkoutCalendar
                date={date}
                handleDateChange={handleDateChange}
                workoutDates={monthData}
                fetchMonthData={fetchMonthData}
                onDataChanged={handleEventChangeData} // 하위 컴포넌트에 데이터 변경 핸들러 전달
            />
            <WorkoutLog
                date={date}
                todayData={todayData}
                handleSaveToday={handleSaveToday}
                fetchMonthData={fetchMonthData}
                handleIsEditing={handleIsEditing}
                onDataChanged={handleEventChangeData} // 하위 컴포넌트에 데이터 변경 핸들러 전달
            />
            {isEditing && (
                <UpdateToday
                    todayId={selectTodayId}
                    handleIsEditing={handleIsEditing}
                    onDataChanged={handleEventChangeData} // 하위 컴포넌트에 데이터 변경 핸들러 전달
                />
            )}
        </div>
    );
};

export default CalenderLogManager;