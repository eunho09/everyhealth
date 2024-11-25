import React, {useEffect} from 'react';
import { IoIosArrowBack } from "react-icons/io";
import "./Modal.css";

const AddRoutineExercise = ({ routineId, exercises, checkedList, setCheckedList, onClose, onSave, sequence, setSequence, lastSequence, fetchExercises }) => {
    const checkHandler = async (exerciseId, isChecked) => {
        if (isChecked) {
            setCheckedList((prev) => [
                ...prev,
                { exerciseId: exerciseId, sequence: sequence },
            ]);
            setSequence((prev) => prev + 1);
        } else {
            const newSequence = lastSequence(routineId);
            setCheckedList((prev) =>
                prev
                    .filter((item) => item.exerciseId !== exerciseId)
                    .map((item, index) => ({
                        ...item,
                        sequence: newSequence + index,
                    }))
            );
            setSequence((prev) => prev - 1);
        }
    };

    useEffect(() => {
        fetchExercises();
    }, [])

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="button-position">
                    <button onClick={() => onClose(routineId)} className="back-button rotate-right"><IoIosArrowBack /></button>
                </div>
                <h3>운동 추가</h3>
                <div className="row-column">
                    {exercises.map((exercise) => (
                        <div key={exercise.id}>
                            <input
                                className="input-routine"
                                type="checkbox"
                                id={`exercise-${exercise.id}`}
                                onChange={(e) => checkHandler(exercise.id, e.target.checked)}
                            />
                            <label htmlFor={`exercise-${exercise.id}`}>{exercise.name}</label>
                        </div>
                    ))}
                </div>
                <button onClick={() => onSave(routineId)}>저장</button>
            </div>
        </div>
    );
};

export default AddRoutineExercise;
