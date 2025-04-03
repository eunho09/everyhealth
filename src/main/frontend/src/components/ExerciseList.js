import React, { useEffect, useState} from 'react';
import "../styles/Modal.css";
import "../styles/ExerciseList.css";
import { FaRegTrashAlt } from "react-icons/fa";
import { TiDeleteOutline } from "react-icons/ti";
import { IoIosArrowBack } from "react-icons/io";
import {exerciseService} from "../services/exerciseService";


const ExerciseList = ({exercises, classification, fetchExercise}) => {
    const [selectedExercise, setSelectedExercise] = useState(null);
    const [isModalOpen, setModelOpen] = useState(false);
    const [classificationBox, setClassificationBox] = useState({});
    const [errors, setErrors] = useState({});

    useEffect(() => {
        const fetchClassificationBox = async () => {
            setClassificationBox(classification);
        }

        fetchClassificationBox();
    }, [])

    const validation = () => {
        const newErrors = {};
        if (!selectedExercise.name.trim()) newErrors.name = "운동 이름을 입력해주세요.";
        if (selectedExercise.repWeightList.map(repWeight => {
            if (repWeight.reps <= 0){
                newErrors.reps = "반복 횟수를 정확히 입력해주세요.";
            }
        }))
            if (selectedExercise.repWeightList.length <= 0) newErrors.repWeights = "운동 세트를 입력해주세요."

        setErrors(newErrors);

        return Object.keys(newErrors).length === 0;
    }


    const openModal = (exercise) => {
        setSelectedExercise(exercise);
        setModelOpen(true);
    };

    const saveAndCloseModal = async (id) => {
        if (!validation()){
            return;
        }
        await handleUpdateExercise(id);
        setSelectedExercise(null);
        setModelOpen(false);
    }

    const closeModal = () => {
        setSelectedExercise(null);
        setModelOpen(false);
        setErrors({});
    };

    const handleChange = (e) => {
        const {id, value} = e.target;
        setSelectedExercise((prev) => ({ ...prev, [id]: value }));

        if (errors[id]){
            setErrors(prev => ({...prev, [id]: ''}))
        }
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

    const handleUpdateExercise = async (id) => {
        try {
            await exerciseService.updateExercise(id, selectedExercise);
            fetchExercise();
        } catch (error) {
            console.error(error);
        }
    }

    const addSet = () => {
        setSelectedExercise((prev) => ({...prev, repWeightList: [...prev.repWeightList, { reps: '', weight: '' }]}));
    };

    const removeSet = (index) => {
        setSelectedExercise((prev) => ({...prev, repWeightList: prev.repWeightList.filter((_, i) => i !== index)}));
    };

    const deleteExercise = async (id) => {
        try {
            await exerciseService.deleteById(id);
            closeModal();
            await fetchExercise();
        } catch(e){
            console.error(e);
        }
    }

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
                        <div className="modal-content">
                            <div className="button-position">
                                <button className="back-button rotate-right" onClick={closeModal}><IoIosArrowBack/>
                                </button>
                            </div>
                            <h2 className="title">수정</h2>
                            <button onClick={() => deleteExercise(selectedExercise.id)}>
                                <FaRegTrashAlt/>
                            </button>
                            <input
                                type="text"
                                id="name"
                                placeholder="운동명"
                                value={selectedExercise.name || ''}
                                onChange={handleChange}
                            />
                            {errors.name && <p className="error-message">{errors.name}</p>}
                            <textarea
                                placeholder="메모"
                                id="memo"
                                value={selectedExercise.memo || ''}
                                rows="3"
                                onChange={handleChange}
                            />
                            <select onChange={handleChange} value={selectedExercise.classification || ''}
                                    id="classification">
                                {Object.entries(classificationBox).map(([key, value]) => (
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
                                        onChange={(e) => {
                                            handleSetChange(index, "reps", e.target.value)
                                            setErrors(prev => ({...prev, reps: ''}))
                                        }}
                                    />
                                    <input
                                        type="number"
                                        placeholder="무게"
                                        value={set.weight}
                                        onChange={(e) => handleSetChange(index, "weight", e.target.value)}
                                    />
                                    <button className="small-button" onClick={() => removeSet(index)}>
                                        <TiDeleteOutline/>
                                    </button>
                                </div>
                            ))}
                            {errors.reps && <p className="error-message">{errors.reps}</p>}
                            {errors.repWeights && <p className="error-message">{errors.repWeights}</p>}
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
