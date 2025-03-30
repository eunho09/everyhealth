import React, {useEffect, useState} from "react"
import Plus from "./Plus";
import "../styles/Modal.css"
import { TiDeleteOutline } from "react-icons/ti";
import {IoIosArrowBack} from "react-icons/io";
import {exerciseService} from "../services/exerciseService";

const AddExercise = ({fetchExercise, classification}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [classificationBox, setClassificationBox] = useState(null);
    const [exerciseData, setExerciseData] = useState({
        name: '',
        note: '',
        repWeights: [{reps:'', weight:''}],
        classification: ''
    });

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    useEffect(() => {
        const fetchClassificationBox = async () => {
            const defaultKey = "가슴";
            if (classification[defaultKey]) {
                setExerciseData(prev => ({...prev, classification: classification[defaultKey]}));
            } else if (Object.keys(classification).length > 0) {
                const firstKey = Object.keys(classification)[0];
                setExerciseData(prev => ({...prev, classification: classification[firstKey]}));
            }
            setClassificationBox(classification);
        }

        fetchClassificationBox();
    }, [])

    const handleSave = async (e) => {
        await handleAddExercise(e);
        closeModal();
        fetchExercise();
    };

    const handleAddExercise = async (e) => {
        e.preventDefault();

        try {
             await exerciseService.saveExercise(exerciseData.name, exerciseData.note, exerciseData.repWeights, exerciseData.classification);
        } catch (error){
            console.error(error);
        }
    }

    const handleSetChange = (index, field, value) => {
        const updatedSets = exerciseData.repWeights.map((set, i) =>
            i === index ? { ...set, [field]: value } : set
        );

        setExerciseData(prev => ({...prev, repWeights: updatedSets}))
    };

    const addSet = () => {
        setExerciseData(prev => ({...prev, repWeights: [...prev.repWeights, {reps: '', weight: ''}]}))
    };

    const removeSet = (index) => {
        setExerciseData(prev => ({
            ...prev,
            repWeights: prev.repWeights.filter((_, i) => i !== index)
        }));
    };

    const handleChange = (e) => {
        const {id, value} = e.target;
        setExerciseData(prev => ({...prev, [id]: value}))
    };

    return (
        <>
            <div className="plus-button" onClick={openModal}>
                <Plus/>
            </div>
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="button-position">
                            <button className="back-button rotate-right" onClick={closeModal}><IoIosArrowBack/></button>
                        </div>
                        <h2>추가</h2>
                        <div className="m-b">
                            <input
                                type="text"
                                placeholder="운동명"
                                id="name"
                                value={exerciseData.name}
                                onChange={handleChange}
                            />
                            <textarea
                                placeholder="메모"
                                id="note"
                                value={exerciseData.note}
                                rows="3"
                                onChange={handleChange}
                            />
                            <select onChange={handleChange} value={exerciseData.classification} id="classification">
                                {Object.entries(classificationBox).map(([key, value]) => (
                                    <option key={key} value={value}>
                                        {key}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <h3>세트</h3>
                        {exerciseData.repWeights.map((repWeight, index) => (
                            <div className="row" key={index}>
                                <div className="number">{index + 1}</div>
                                <input
                                    className="small-input"
                                    type="number"
                                    placeholder={`반복 횟수 (Set ${index + 1})`}
                                    value={repWeight.reps}
                                    onChange={(e) =>
                                        handleSetChange(index, 'reps', e.target.value)
                                    }
                                />
                                <input
                                    className="small-input"
                                    type="number"
                                    placeholder={`무게 (Set ${index + 1})`}
                                    value={repWeight.weight}
                                    onChange={(e) =>
                                        handleSetChange(index, 'weight', e.target.value)
                                    }
                                />
                                <button className="small-button" onClick={() => removeSet(index)}>
                                    <TiDeleteOutline/>
                                </button>
                            </div>
                        ))}
                        <button onClick={addSet}>세트 추가</button>
                        <button onClick={handleSave}>저장</button>
                    </div>
                </div>
            )}
        </>
    );
}
export default AddExercise;