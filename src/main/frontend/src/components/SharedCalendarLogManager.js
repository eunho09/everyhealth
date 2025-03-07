import React, {useCallback, useEffect, useState} from 'react';
import { useNavigate } from "react-router-dom";
import {friendService} from "../services/friendService";
import {todayService, todayService as sharedTodayService} from "../services/todayService";
import WorkoutCalendar from "./WorkoutCalendar";
import SharedWorkoutLog from "./SharedWorkoutLog";
import moment from "moment/moment";

const SharedCalendarLogManager = ({ friendId }) => {
    const [date, setDate] = useState(new Date());
    const [monthData, setMonthData] = useState([]);
    const [todayData, setTodayData] = useState();
    const [isFriend, setIsFriend] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const checkFriendship = async () => {
            try {
                setIsLoading(true);
                const data = await friendService.checkFriendShip(friendId);
                setIsFriend(data);

                if (!data) {
                    console.log("접근 권한이 없습니다.");
                }
            } catch (error) {
                console.error("친구 관계 확인 실패:", error);
                setIsFriend(false);
            } finally {
                setIsLoading(false);
            }
        };

        checkFriendship();
    }, [friendId, navigate]);

    const fetchMonthData = useCallback(async (month) => {
        if (!isFriend) return;

        try {
            const targetMonth = month || date.getMonth() + 1;
            const data = await sharedTodayService.findByFriendAndMonth(friendId, targetMonth);
            setMonthData(data);
        } catch (e) {
            console.error("월 데이터 가져오기 실패:", e);
        }
    }, [date, friendId, isFriend]);

    useEffect(() => {
        if (isFriend) {
            const month = date.getMonth() + 1;
            fetchMonthData(month);
        }
    }, [fetchMonthData, isFriend]);


    useEffect(() => {
        const dateFormat = moment(date).format("YYYY-MM-DD");
        if (hasToday(dateFormat)){
            const fetchDateData = async () => {
                try {
                    const data = await todayService.getTodayDateByFriend(friendId, dateFormat);
                    setTodayData(data);
                } catch (e) {
                    console.error("날짜 데이터 가져오기 실패:", e);
                }
            };
            fetchDateData();
        } else {
            setTodayData(undefined);
        }
    }, [date, monthData]);

    const hasToday = (dateFormat) => {
        return monthData.find(data => data.localDate === dateFormat);
    };

    const handleDateChange = (newDate) => {
        setDate(newDate);
    };

    if (isLoading) {
        return <div>로딩 중...</div>;
    }

    if (!isFriend) {
        return (
            <div className="access-denied">
                <h2>접근 권한이 없습니다</h2>
                <p>이 사용자의 운동 기록을 볼 수 있는 권한이 없습니다.</p>
                <button onClick={() => navigate("/")}>홈으로 돌아가기</button>
            </div>
        );
    }

    return (
        <div className="flex">
            <WorkoutCalendar
                date={date}
                handleDateChange={handleDateChange}
                workoutDates={monthData}
                fetchMonthData={fetchMonthData}
            />
            <SharedWorkoutLog
                date={date}
                todayData={todayData}
            />
        </div>
    );
};

export default SharedCalendarLogManager;