import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RoutineList from '../RoutineList';
import AddRoutineExercise from '../AddRoutineExercise';
import UpdateRoutine from "../UpdateRoutine";
import {addRoutineExercise, findRoutineById, findRoutineByMemberId} from "../../api/routineApi";
import {findExerciseByMemberId} from "../../api/exerciseApi";

const RoutineManager = () => {
    const [routines, setRoutines] = useState([]);
    const [exercises, setExercises] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRoutineId, setSelectedRoutineId] = useState(null);
    const [checkedList, setCheckedList] = useState([]);
    const [sequence, setSequence] = useState(1);
    const [toggleState, setToggleState] = useState({}); // 토글 상태 관리
    const [isEditing, setIsEditing] = useState(false);

    // 루틴 데이터 로드
    useEffect(() => {
        // 비동기 함수 정의
        const fetchRoutineData = async () => {
            try {
                const data = await findRoutineByMemberId();
                setRoutines(data);
                console.log(data);
            } catch (error) {
                console.error(error);
            }
        };

        // 비동기 함수 호출
        fetchRoutineData();
    }, []);

        // 추가할 운동 데이터 로드
        const fetchExercises = async () => {
            try {
                const data = await findExerciseByMemberId();
                setExercises(data);
            } catch (error) {
                console.error("운동 데이터 로드 중 오류 발생:", error);
            }
        };


    const openModal = (routineId) => {
        setSelectedRoutineId(routineId);
        initSequence(routineId)
        setIsModalOpen(true);
        setCheckedList([]);
    };

    const closeModal = (routineId) => {
        initSequence(routineId);
        setIsModalOpen(false);
        setCheckedList([]);
    };

    const getSequence = (routineId) => {
        console.log("routineId : " + routineId)
        const routine = routines.find((r) => r.routineId === routineId);
        const lastSequence = routine.routineExerciseDtoList.length > 0
            ? Math.max(...routine.routineExerciseDtoList.map((e) => e.sequence))
            : 0;

        return lastSequence;
    }

    const initSequence = (routineId) => {

        const findSequence = getSequence(routineId);

        setSequence(findSequence + 1);
        console.log("initSequence : " + findSequence)
    }

    const lastSequence = (routineId) => {
        const findSequence = getSequence(routineId);
        return findSequence + 1
    };


    // 토글 상태 변경 핸들러
    const toggleExercises = (routineId) => {
        setToggleState((prevState) => ({
            ...prevState,
            [routineId]: !prevState[routineId], // 토글 상태 변경
        }));
    };


    const saveExercises = async (routineId) => {
        console.log("routine Id" + routineId);
        checkedList.map((id, value) => {
            console.log("id : " + id, " / value : " + value);
        })

        try {
            await addRoutineExercise(routineId, checkedList);

            const data = await findRoutineById();
            setRoutines(data);

            closeModal(routineId);
        } catch (error) {
            setRoutines([]); // 실패 시 빈 배열로 초기화
            console.error(error);
        }

        window.location.reload();
    };

    useEffect(() => {
        console.log(checkedList);
    },[checkedList])

    const editingOpen = (routineId) => {
        setSelectedRoutineId(routineId);
        setIsEditing(true);
        console.log(routineId);
    }

    const editingClose = () => {
        setIsEditing(false);
    }

    return (
        <div>
            <h2>루틴 목록</h2>
            <RoutineList
                routines={routines}
                toggleState={toggleState}
                onToggle={toggleExercises}
                open={openModal}
                edit={editingOpen}
            />
            {isModalOpen && (
                <AddRoutineExercise
                    routineId={selectedRoutineId}
                    exercises={exercises}
                    checkedList={checkedList}
                    setCheckedList={setCheckedList}
                    sequence={sequence}
                    setSequence={setSequence}
                    lastSequence={lastSequence}
                    onClose={closeModal}
                    onSave={saveExercises}
                    fetchExercises={fetchExercises}
                />
            )}
            {isEditing && (
                <UpdateRoutine
                    routineId={selectedRoutineId}
                    close={editingClose}
                />
            )}
        </div>
    );
};

export default RoutineManager;
