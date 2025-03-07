import React from 'react';

const SharedWorkoutLog = ({date, todayData}) => {

    const formatter = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Seoul' }); // 한국 시간으로 포멧팅
    const formattedDate = formatter.format(date); // YYYY-MM-DD 형식 반환

    return (
        <div className="workout-log">
            <h2>{formattedDate} 운동 일지</h2>
            {todayData && todayData.todayExercises.length > 0 ? (
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
        </div>
    );
};

export default SharedWorkoutLog;