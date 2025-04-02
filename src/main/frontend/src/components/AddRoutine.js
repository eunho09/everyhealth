import React, { useState } from 'react';
import Plus from "./Plus";
import { IoIosArrowBack } from "react-icons/io";
import "../styles/Modal.css";
import {routineService} from "../services/routineService";

const AddRoutine = ({fetchRoutines}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [name, setName] = useState('');
    const [errors, setErrors] = useState({name: ""});

    const validation = () => {
        if (!name.trim()){
            setErrors({name : '루틴 이름을 입력해주세요.'});
            return false;
        }

        return true;
    }

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setErrors({});
    };

    const save = async () => {
        if (!validation()){
            console.log("에러");
            return;
        }

        await saveRoutine();
        setName('');
        setIsModalOpen(false);
        await fetchRoutines();
    };

    const saveRoutine = async () => {
        try {
            await routineService.save(name)
        } catch (error){
            console.error(error);
            throw error;
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
                            onChange={(e) => {
                                setName(e.target.value)
                                setErrors(prev => ({...prev, name: ''}))
                            }
                            }
                        />
                        {errors.name && <p className="error-message">{errors.name}</p>}
                        <button onClick={() => save()}>저장</button>
                    </div>
                </div>
            )}
        </>
    );
};

export default AddRoutine;
