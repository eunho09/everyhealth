import React, { useEffect, useState } from 'react';
import axios from 'axios';
import "../styles/Modal.css";
import "../styles/ExerciseList.css";
import { TiDeleteOutline } from "react-icons/ti";
import { IoIosArrowBack } from "react-icons/io";
import {exerciseService} from "../services/exerciseService";


const ExerciseList = () => {
    const [exercises, setExercises] = useState([]);
    const [selectedExercise, setSelectedExercise] = useState(null);
    const [isModalOpen, setModelOpen] = useState(false);
    const [selectBox, setSelectBox] = useState({});

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
        const fetchExercise = async () => {
            try {
                const data = await exerciseService.findExerciseByMemberId();
                console.log(data);
                setExercises(data);
            } catch (error){
                console.error(error);
            }
        }

        fetchExercise();
    }, []);

    const handleExerciseChange = (field, value) => {
        setSelectedExercise((prev) => ({ ...prev, [field]: value }));
    };

    const handleSetChange = (index, field, value) => {
        const updatedRepWeightList = selectedExercise.repWeightList.map((repWeight, i) => {
            if (i === index) {
                return {
                    ...repWeight,
                    [field]: value
                }
            }

            return repWeight;
        });

        setSelectedExercise((prev) => ({
            ...prev,
            repWeightList: updatedRepWeightList
        }));
    };


    useEffect(() => {
        console.log(selectedExercise);
    }, [selectedExercise])

    const handleUpdateExercise = async (id) => {
        try {
            const data = await exerciseService.updateExercise(id, selectedExercise);
            window.location.reload();
            console.log(data)
        } catch (error) {
            console.error(error);
        }
    }

    const addSet = () => {
        setSelectedExercise((prev) => ({
            ...prev,
            repWeightList: [...prev.repWeightList, { reps: '', weight: '' }]
        }));
    };

    const removeSet = (index) => {
        setSelectedExercise((prev) => ({
            ...prev,
            repWeightList: prev.repWeightList.filter((_, i) => i !== index)
        }));
    };

    useEffect(() => {
        const fetchSelectBox = async () => {
            const data = await exerciseService.getClassification();
            console.log(data);
            setSelectBox(data);
        }

        fetchSelectBox();
    }, [])

    return (
        <div className="exercise-list">
            {/* 부위별로 운동 그룹화 */}
            {Object.entries({
                "가슴": exercises.filter(ex => ex.classification === "CHEST"),
                "등": exercises.filter(ex => ex.classification === "BACK"),
                "어깨": exercises.filter(ex => ex.classification === "SHOULDER"),
                "이두": exercises.filter(ex => ex.classification === "BICEPS"),
                "삼두": exercises.filter(ex => ex.classification === "TRICEPS"),
                "하체": exercises.filter(ex => ex.classification === "LOWERBODY"),
                "복근": exercises.filter(ex => ex.classification === "ABS"),
            }).map(([part, exercises]) => (
                exercises.length > 0 && (
                    <div key={part} className="exercise-group">
                        <h3>{part}</h3>
                        <ul>
                            {exercises.map((exercise, index) => (
                                <li key={index}>
                                    <button className="text-button" onClick={() => openModal(exercise)}>
                                        {exercise.name}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </div>
                )
            ))}

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
                            <select onChange={(e) => handleExerciseChange("classification", e.target.value)} value={selectedExercise.classification || ''}>
                                {Object.entries(selectBox).map(([key, value]) => (
                                    <option key={key} value={value}>
                                        {key}
                                    </option>
                                ))}
                            </select>
                            <h3>세트</h3>
                            {selectedExercise.repWeightList && selectedExercise.repWeightList.map((set, index) => (
                                <div className="row" key={index}>
                                    <div className="number">{index + 1}</div>
                                    <input
                                        type="number"
                                        placeholder="반복 횟수"
                                        value={set.reps}
                                        onChange={(e) => handleSetChange(index, "reps", e.target.value)}
                                    />
                                    <input
                                        type="number"
                                        placeholder="무게"
                                        value={set.weight}
                                        onChange={(e) => handleSetChange(index, "weight", e.target.value)}
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
