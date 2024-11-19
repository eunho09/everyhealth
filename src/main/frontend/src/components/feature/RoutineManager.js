import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RoutineList from '../RoutineList';
import AddRoutineExercise from '../AddRoutineExercise';
import UpdateRoutine from "../UpdateRoutine";

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
                const response = await axios.get("/api/member/1/routines");
                setRoutines(response.data); // 상태 업데이트
                console.log(response.data); // 응답 데이터 출력
            } catch (error) {
                console.error(error); // 에러 처리
            }
        };

        // 비동기 함수 호출
        fetchRoutineData();
    }, []);

    // 운동 데이터 로드
    useEffect(() => {
        const fetchExercises = async () => {
            try {
                const response = await axios.get("/api/exercises");
                setExercises(response.data);
            } catch (error) {
                console.error("운동 데이터 로드 중 오류 발생:", error);
            }
        };

        fetchExercises();
    }, []);


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


    const saveExercises = (routineId) => {
        console.log("routine Id" + routineId);
        checkedList.map((id, value) => {
            console.log("id : " + id, " / value : " + value);
        })

        axios.post(`/api/routineExercise`, {
            routineId: routineId,
            exerciseInfoList: checkedList,
        })
            .then(() => axios.get("/api/routine/1"))
            .then((response) => {
                setRoutines(response.data);
                closeModal(routineId);
            })
            .catch((error) => console.error(error));

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
            <h2>운동 목록</h2>
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
