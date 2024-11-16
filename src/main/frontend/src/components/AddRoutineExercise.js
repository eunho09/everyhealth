import React, {useEffect, useState} from 'react';
import axios from "axios";
import {IoIosArrowBack} from "react-icons/io";

const AddRoutineExercise = () => {

    const [exercises, setExercises] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [checkedList, setCheckedList] = useState([]); // 초기값을 빈 배열로 설정
    const [index, setIndex] = useState(1);

    const checkedExerciseHandler = (exerciseId, isChecked) => {
        if (isChecked) {
            // 체크된 경우, 새로운 항목을 추가
            setCheckedList((prev) => [...prev, { id: exerciseId, index: index }]);
            setIndex((prevIndex) => prevIndex + 1);
        } else {
            // 체크 해제된 경우, 해당 항목을 제거
            console.log("체크 해제");
            setCheckedList((prev) => prev.filter((exercise) => exercise.id !== exerciseId));
            setIndex((prevIndex) => prevIndex - 1);
        }
    };

    const checkHandler = (e, exerciseId) => {
        const isChecked = e.target.checked;
        checkedExerciseHandler(exerciseId, isChecked);
    };

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setCheckedList([]);
        setIndex(1);
    };

    useEffect(async () => {
        try {
            const response = await axios.get("/api/member/1/exercises");
            setExercises(response.data);
        } catch (error){
            console.error(error);
        }
    }, [])

    useEffect(() => {
        axios.get("/api/exercises")
            .then((response) => setExercises(response.data))
            .catch((error) => console.log(error));
    }, []);

    return (
        <>{isModalOpen && (
            <div>
                <h3>운동</h3>
                <div className="button-position">
                    <button className="back-button rotate-right" onClick={closeModal}><IoIosArrowBack/></button>
                </div>
                {exercises.map((exercise, idx) => (
                    <div className="row-routine" key={idx}>
                        <input
                            className="small-checkbox"
                            type="checkbox"
                            id={exercise.name}
                            onChange={(e) => checkHandler(e, exercise.id)}
                        />
                        <label htmlFor={exercise.name}>{exercise.name}</label>
                    </div>
                ))}
                <button onClick={() => save()}>저장</button>
            </div>
        )}
        </>
    );
};

export default AddRoutineExercise;