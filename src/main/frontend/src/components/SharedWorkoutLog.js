import React from 'react';

const SharedWorkoutLog = ({date, todayData}) => {
    const formatter = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Seoul' });
    const formattedDate = formatter.format(date);

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
                                    {todayExercise.repWeightList.map((repWeight, setIndex) => (
                                        <li key={setIndex}>
                                            {setIndex + 1}세트: {repWeight.reps}회 {repWeight.weight !== 0 && `${repWeight.weight}kg`}
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