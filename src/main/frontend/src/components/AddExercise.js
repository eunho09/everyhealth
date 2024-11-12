import React, {useState} from "react"
import Plus from "./Plus";
import "./AddExercise.css";
import axios from "axios";
import { TiDeleteOutline } from "react-icons/ti";
import {IoIosArrowBack} from "react-icons/io";

const AddExercise = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [exerciseName, setExerciseName] = useState('');
    const [note, setNote] = useState('');
    const [sets, setSets] = useState([{reps:'', weight:''}]);


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
            const response = await axios.post("/api/exercise", {
                name: exerciseName,
                memberId: 1,
                memo: note,
                repWeight: repWeight,
                classification: "가슴운동",
            });
            console.log(response.data);
        } catch (error){
            console.error(error);
            alert("error");
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
                                    <TiDeleteOutline />
                                </button>
                            </div>
                        ))}
                        <button onClick={addSet}>세트 추가</button>
                        <button onClick={handleSave}>저장</button>
                    </div>
                </div>
            )}
        </>
    )
}
export default AddExercise;