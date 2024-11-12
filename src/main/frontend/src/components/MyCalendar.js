import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import styled from 'styled-components';
import moment from "moment";

const CalendarStyled = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  
  /*
  //calender
  .react-calendar{ 
    border: 1px solid #a0a096;
    border-radius: 10px;
    width: 40rem;
  }
  
  //day
  .react-calendar__tile{
    padding: 2rem;
    border-radius: 50%;
  }
  
  //title
  .react-calendar__navigation button{ 
    min-width: 5rem;
    font-size: 1.5rem;
    font-weight: bold;
  }*/

  /* 전체 캘린더 */
  .react-calendar {
    width: 100%;
    max-width: 700px;
    background: #f7f8fa;
    border: 1px solid #e6e6e6;
    border-radius: 12px;
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
    font-family: 'Helvetica Neue', sans-serif;
    padding: 20px;
  }

  /* 캘린더 네비게이션 (월 이동 화살표와 타이틀) */
  .react-calendar__navigation button {
    background: none;
    border: none;
    color: #4a4a4a;
    font-size: 16px;
    font-weight: bold;
    padding: 10px;
    cursor: pointer;
  }

  .react-calendar__navigation button:disabled {
    color: #c0c0c0;
  }

  .react-calendar__navigation button:enabled:hover {
    color: #007bff;
  }

  /* 요일 헤더 스타일 */
  .react-calendar__month-view__weekdays {
    font-weight: bold;
    font-size: 14px;
    color: #6d6d6d;
    text-align: center;
    margin-bottom: 10px;
  }

  .react-calendar__month-view__weekdays__weekday {
    padding: 8px;
  }

  /* 날짜 셀 스타일 */
  .react-calendar__tile {
    background: #fff;
    padding: 30px;
    text-align: center;
    transition: background 0.3s, color 0.3s;
    box-sizing: border-box;
  }

  /* tileContent가 포함될 경우 오버플로우 방지 */
  .react-calendar__tile-content {
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    border-radius: 50%;
    width: 10px; 
    height: 10px; 
    background: blue; 
    margin: 0 10px;
  }

  .react-calendar__tile:enabled:hover {
    background: #e6f7ff;
    color: #007bff;
    cursor: pointer;
  }

  .react-calendar__tile--active {
    background: #007bff;
    color: #fff;
  }

  .react-calendar__tile--now {
    background: #ffe6e6;
    color: #ff4d4d;
    font-weight: bold;
  }

  .react-calendar__tile--hasActive {
    background: #d1eaff;
    color: #007bff;
  }

  /* 반응형 스타일 */
  @media (max-width: 500px) {
    .react-calendar {
      font-size: 14px;
    }
  }

`

const MyCalendar = () => {
    const [date, setDate] = useState(new Date());

    const handleDateChange = (newDate) => {
        setDate(newDate);
    };

    // 예시 데이터: 운동한 날짜 목록
    const workoutDates = ['2024-11-08', '2024-11-09'];

    // 날짜에 동그라미 표시를 추가하는 함수
    const tileContent = ({ date, view }) => {
        if (view === 'month') {
            const dateString = date.toISOString().split('T')[0];
            if (workoutDates.includes(dateString)) {
                // return <div style={{ borderRadius: '50%', width: '10px', height: '10px', background: 'blue', margin: '0 auto' }}></div>;
                return <div className="react-calendar__tile-content"></div>
            }
        }
        return null;
    };

    return (
        <CalendarStyled>
            <Calendar
                onChange={handleDateChange}
                value={date}
                formatDay={(locale, date) => moment(date).format("D")}
                formatMonthYear={(locale, date) => moment(date).format("YYYY. MM")} // 네비게이션에서 2023. 12 이렇게 보이도록 설정
                calendarType="gregory" // 일요일 부터 시작
                next2Label={null} // +1년 & +10년 이동 버튼 숨기기
                prev2Label={null} // -1년 & -10년 이동 버튼 숨기기
                tileContent={tileContent}
             />
        </CalendarStyled>
    );
};

export default MyCalendar;
