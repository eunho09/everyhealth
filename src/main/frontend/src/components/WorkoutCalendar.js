import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import styled from 'styled-components';
import moment from "moment";

const CalendarStyled = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;

    .react-calendar {
        width: 100%;
        max-width: 800px;
        background: white;
        border: none;
        border-radius: 16px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        padding: 24px;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;

        /* 달력 상단 네비게이션 스타일 */
        .react-calendar__navigation {
            margin-bottom: 24px;

            button {
                min-width: 44px;
                background: none;
                border: none;
                color: #1a1a1a;
                font-size: 18px;
                font-weight: 600;
                padding: 8px 12px;
                border-radius: 8px;
                cursor: pointer;
                transition: all 0.2s ease;

                &:disabled {
                    opacity: 0.5;
                }

                &:enabled:hover,
                &:enabled:focus {
                    background-color: #f0f0f0;
                    color: #0066ff;
                }
            }
        }

        /* 요일 헤더 스타일 */
        .react-calendar__month-view__weekdays {
            text-transform: uppercase;
            font-weight: 600;
            font-size: 14px;
            color: #666;

            abbr {
                text-decoration: none;
                border: none;
            }
        }

        /* 날짜 셀 스타일 */
        .react-calendar__tile {
            aspect-ratio: 1;
            max-width: 100%;
            padding: 10px;
            background: none;
            text-align: center;
            line-height: 1.6;
            font-size: 16px;
            position: relative;

            &:enabled:hover,
            &:enabled:focus {
                background-color: #f5f5f5;
                border-radius: 12px;
            }

            /* 오늘 날짜 스타일 */
            &.react-calendar__tile--now {
                background: #fff3f3;
                border-radius: 12px;
                font-weight: 600;
                color: #ff4d4f;
            }

            /* 선택된 날짜 스타일 */
            &.react-calendar__tile--active {
                background: #1890ff;
                border-radius: 12px;
                color: white;
                font-weight: 600;

                &:enabled:hover,
                &:enabled:focus {
                    background: #096dd9;
                }
            }
        }
    }

    .react-calendar__tile-content {
        position: absolute;
        bottom: 4px;
        left: 50%;
        transform: translateX(-50%);
        width: 6px;
        height: 6px;
        background: transparent;
        border: 1px solid red;
        border-radius: 50%;
    }

    .react-calendar__tile-content-red {
        position: absolute;
        bottom: 4px;
        left: 50%;
        transform: translateX(-50%);
        width: 6px;
        height: 6px;
        background: red;
        border-radius: 50%;
    }

    /* 반응형 스타일 */
    @media (max-width: 768px) {
        .react-calendar {
            padding: 16px;
            max-width: 100%;
        }

        .react-calendar__tile {
            font-size: 14px;
            padding: 8px;
        }

        .react-calendar__navigation button {
            font-size: 16px;
        }
    }

    @media (max-width: 480px) {
        .react-calendar {
            padding: 12px;
        }

        .react-calendar__tile {
            font-size: 12px;
            padding: 6px;
        }
    }
`;

const WorkoutCalendar = ({date, handleDateChange, workoutDates, fetchMonthData}) => {


    // 날짜에 동그라미 표시를 추가하는 함수
    const tileContent = ({ date, view }) => {
        if (view === 'month') {
            const formatter = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Seoul' }); //한국 시간으로 포멧팅
            const dateString = formatter.format(date); // YYYY-MM-DD 형식 반환
            if (Array.isArray(workoutDates)) {
                const workoutData = workoutDates.find(workout => {
                    return workout.localDate === dateString;
                });

                if (workoutData?.checkBox === 'True') {
                    return <div className="react-calendar__tile-content-red"></div>;
                } else if (workoutData?.checkBox === 'False') {
                    return <div className="react-calendar__tile-content"></div>;
                }
            }
        }
        return null;
    };

    const handleActiveStartDateChange = ({ activeStartDate }) => {
        if (activeStartDate) {
            const month = activeStartDate.getMonth() + 1;
            fetchMonthData(month);
        }
    };

    return (
        <>
            <CalendarStyled>
                <Calendar
                    onChange={handleDateChange}
                    locale="ko"
                    value={date}
                    formatDay={(locale, date) => moment(date).format("D")}
                    formatMonthYear={(locale, date) => moment(date).format("YYYY. MM")} // 네비게이션에서 2023. 12 이렇게 보이도록 설정
                    calendarType="gregory" // 일요일 부터 시작
                    next2Label={null} // +1년 & +10년 이동 버튼 숨기기
                    prev2Label={null} // -1년 & -10년 이동 버튼 숨기기
                    tileContent={tileContent}
                    onActiveStartDateChange={handleActiveStartDateChange}  // 추가된 부분
                 />
            </CalendarStyled>
        </>
    );
};

export default WorkoutCalendar;
