import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AddExercise.css';
import { TiDeleteOutline } from "react-icons/ti";
import { IoIosArrowBack } from "react-icons/io";


const ExerciseList = () => {
    const [exercises, setExercises] = useState([]);
    const [selectedExercise, setSelectedExercise] = useState(null);
    const [isModalOpen, setModelOpen] = useState(false);

    const openModal = (exercise) => {
        setSelectedExercise(exercise);
        setModelOpen(true);
    };

    const saveAndCloseModal = async (id) => {
        await handleUpdateExercise(id);
        setSelectedExercise(null);
        setModelOpen(false);
    }

    const closeModal = async () => {
        setSelectedExercise(null);
        setModelOpen(false);
    };

    useEffect(() => {
        axios.get('/api/exercises')
            .then((response) => {
                setExercises(response.data);

                console.log(exercises);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

    const handleExerciseChange = (field, value) => {
        setSelectedExercise((prev) => ({ ...prev, [field]: value }));
    };

    const handleSetChange = (index, subIndex, value) => {
        const updatedSets = selectedExercise.repWeight.map((set, i) =>
            i === index ? set.map((item, j) => (j === subIndex ? value : item)) : set
        );
        setSelectedExercise((prev) => ({ ...prev, repWeight: updatedSets }));
    };


    useEffect(() => {
        console.log(selectedExercise);
    }, [selectedExercise])

    const handleUpdateExercise = async (id) => {
        try {
            const response = await axios.patch(`/api/exercise/${id}`, {
                repWeight: selectedExercise.repWeight
            })

            window.location.reload();
            console.log(response)
        } catch (error) {
            console.error(error);
        }
    }

    const addSet = () => {
        setSelectedExercise((prev) => ({
            ...prev,
            repWeight: [...prev.repWeight, { reps: '', weight: '' }]
        }));
    };

    const removeSet = (index) => {
        setSelectedExercise((prev) => ({
            ...prev,
            repWeight: prev.repWeight.filter((_, i) => i !== index)
        }));
    };

    return (
        <div className="exercise-list">
            <h2>운동 목록</h2>
            <ul>
                {exercises.map((exercise, index) => (
                    <li key={index}>
                        <button className="text-button" onClick={() => openModal(exercise)}>
                            {exercise.name}
                        </button>
                    </li>
                ))}
            </ul>

            {isModalOpen && selectedExercise && (
                <div>
                    <div className="modal-overlay">
                        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                            <div className="button-position">
                                <button className="back-button rotate-right" onClick={closeModal}><IoIosArrowBack/></button>
                            </div>
                            <h2 className="title">수정</h2>
                            <input
                                type="text"
                                placeholder="운동명"
                                value={selectedExercise.name || ''}
                                onChange={(e) => handleExerciseChange('name', e.target.value)}
                            />
                            <textarea
                                placeholder="메모"
                                value={selectedExercise.memo || ''}
                                rows="3"
                                onChange={(e) => handleExerciseChange('memo', e.target.value)}
                            />
                            <h3>세트</h3>
                            {selectedExercise.repWeight && selectedExercise.repWeight.map((set, index) => (
                                <div className="row" key={index}>
                                    <div className="number">{index + 1}</div>
                                    <input
                                        type="number"
                                        placeholder="반복 횟수"
                                        value={set[0]}
                                        onChange={(e) => handleSetChange(index, 0, e.target.value)}
                                    />
                                    <input
                                        type="number"
                                        placeholder="무게"
                                        value={set[1]}
                                        onChange={(e) => handleSetChange(index, 1, e.target.value)}
                                    />
                                    <button className="small-button" onClick={() => removeSet(index)}>
                                        <TiDeleteOutline />
                                    </button>
                                </div>
                            ))}
                            <button onClick={addSet}>세트 추가</button>
                            <button onClick={() => saveAndCloseModal(selectedExercise.id)}>저장</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ExerciseList;
