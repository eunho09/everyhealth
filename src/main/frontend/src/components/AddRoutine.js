import React, { useEffect, useState } from 'react';
import axios from "axios";
import Plus from "./Plus";
import { IoIosArrowBack } from "react-icons/io";

const AddRoutine = () => {
    const [exercises, setExercises] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [name, setName] = useState('');
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

    useEffect(() => {
        axios.get("/api/exercises")
            .then((response) => setExercises(response.data))
            .catch((error) => console.log(error));
    }, []);


    const save = () => {
        saveRoutine();
        setIsModalOpen(false);
        window.location.reload();
    };

    const saveRoutine = async () => {
        try {
            const response = await axios.post("/api/routine", {
                name:name,
                memberId:1
            })
            console.log(response.data);
        } catch (error){
            console.error(error);
        }
    }

    return (
        <>
            <div className="plus-button" onClick={openModal}>
                <Plus />
            </div>
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="button-position">
                            <button className="back-button rotate-right" onClick={closeModal}><IoIosArrowBack /></button>
                        </div>
                        <h2>추가</h2>
                        <input
                            type="text"
                            placeholder="루틴명"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                       {/* <h3>운동</h3>
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
                        ))}*/}
                        <button onClick={() => save()}>저장</button>
                    </div>
                </div>
            )}
        </>
    );
};

export default AddRoutine;
