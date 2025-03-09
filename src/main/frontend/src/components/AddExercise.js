import React, {useEffect, useState} from "react"
import Plus from "./Plus";
import "../styles/Modal.css"
import axios from "axios";
import { TiDeleteOutline } from "react-icons/ti";
import {IoIosArrowBack} from "react-icons/io";
import {exerciseService} from "../services/exerciseService";

const AddExercise = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [exerciseName, setExerciseName] = useState('');
    const [note, setNote] = useState('');
    const [sets, setSets] = useState([{reps:'', weight:''}]);
    const [selectBox, setSelectBox] = useState({});
    const [selectClassification, setSelectClassification] = useState();


    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const handleSave = async (e) => {
        await handleAddExercise(e);
        window.location.reload();
        closeModal();
    };

    const handleAddExercise = async (e) => {
        e.preventDefault();

        const repWeight = sets.map(set => [set.reps, set.weight])

        try {
            const data = await exerciseService.saveExercise(exerciseName, note, repWeight, selectClassification);
        } catch (error){
            console.error(error);
        }
    }

    const handleSetChange = (index, field, value) => {
        const updatedSets = sets.map((set, i) =>
            i === index ? { ...set, [field]: value } : set
        );
        setSets(updatedSets);
    };

    const addSet = () => {
        setSets([...sets, { reps: '', weight: '' }]);
    };

    const removeSet = (index) => {
        const updatedSets = sets.filter((_, i) => i !== index);
        setSets(updatedSets);
    }

    useEffect(() => {
        const fetchSelectBox = async () => {
            const data = await exerciseService.getClassification();
            const defaultKey = "가슴";
            if (data[defaultKey]) {
                setSelectClassification(data[defaultKey]);
            } else if (Object.keys(data).length > 0) {
                const firstKey = Object.keys(data)[0];
                setSelectClassification(data[firstKey]);
            }
            setSelectBox(data);
        }

        fetchSelectBox();
    }, [])

    const handleSelect = (e) => {
        setSelectClassification(e.target.value);
    }

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
                                value={exerciseName}
                                onChange={(e) => setExerciseName(e.target.value)}
                            />
                            <textarea
                                placeholder="메모"
                                value={note}
                                rows="3"
                                onChange={(e) => setNote(e.target.value)}
                            />
                            <select onChange={handleSelect} value={selectClassification}>
                                {Object.entries(selectBox).map(([key, value]) => (
                                    <option key={key} value={value}>
                                        {key}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <h3>세트</h3>
                        {sets.map((set, index) => (
                            <div className="row" key={index}>
                                <div className="number">{index + 1}</div>
                                <input
                                    className="small-input"
                                    type="number"
                                    placeholder={`반복 횟수 (Set ${index + 1})`}
                                    value={set.reps}
                                    onChange={(e) =>
                                        handleSetChange(index, 'reps', e.target.value)
                                    }
                                />
                                <input
                                    className="small-input"
                                    type="number"
                                    placeholder={`무게 (Set ${index + 1})`}
                                    value={set.weight}
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