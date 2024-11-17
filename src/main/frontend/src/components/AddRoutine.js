import React, { useEffect, useState } from 'react';
import axios from "axios";
import Plus from "./Plus";
import { IoIosArrowBack } from "react-icons/io";
import "./Modal.css";

const AddRoutine = () => {

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [name, setName] = useState('');

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };


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
                        <button onClick={() => save()}>저장</button>
                    </div>
                </div>
            )}
        </>
    );
};

export default AddRoutine;
