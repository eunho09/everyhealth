import React, { useEffect, useState } from "react";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { IoIosArrowBack } from "react-icons/io";
import { TiDeleteOutline } from "react-icons/ti";
import axios from "axios";
import {todayService} from "../services/todayService";

const UpdateToday = ({ onDataChanged, todayId, handleIsEditing }) => {
    const [today, setToday] = useState(null);
    const [isLoading, setIsLoading] = useState(true); // 로딩 상태
    const [todayExerciseList, setTodayExerciseList] = useState([]);
    const [repWeightList, setRepWeightList] = useState([]);

    const handleSetChange = (exerciseIndex, setIndex, field, value) => {
        // 상태를 복사하여 변경 작업
        const updatedSets = [...repWeightList]; // repWeight 배열 복사
        console.log("updatedSets", updatedSets)
        console.log("exerciseIndex", exerciseIndex)
        const numberValue = value ? parseFloat(value) : null;

        if (field === 'reps') {
            updatedSets[exerciseIndex][setIndex].reps = numberValue;
        } else if (field === 'weight') {
            updatedSets[exerciseIndex][setIndex].weight = numberValue;
        }

        setRepWeightList(updatedSets); // 변경된 배열로 상태 업데이트
    };

    const removeSet = (exerciseIndex, setIndex) => {
        const updatedSets = repWeightList.map((sets, i) =>
            i === exerciseIndex ? sets.filter((_, j) => j !== setIndex) : sets
        );
        setRepWeightList(updatedSets);
    };

    const removeExercise = (exerciseIndex, todayExerciseId) => {
        handleDeleteRoutineExercise(todayExerciseId);

        // 운동 삭제
        const updatedExercises = todayExerciseList.filter((_, i) => i !== exerciseIndex);
        setTodayExerciseList(updatedExercises);

        // 해당 운동의 세트 정보 삭제
        const updatedRepWeight = repWeightList.filter((_, i) => i !== exerciseIndex);
        console.log("exerciseIndex", exerciseIndex);
        setRepWeightList(updatedRepWeight);
    };

    const handleDeleteRoutineExercise = async (todayExerciseId) => {
        try {
            const data = await todayService.deleteTodayExercise(todayExerciseId);
            console.log(data);
        } catch (error) {
            console.error(error);
        }
    };

    const onDragEnd = (result) => {
        if (!result.destination) return; // 드래그를 놓지 않은 경우 무시

        // routineExerciseList의 순서를 변경
        const reorderedExercises = Array.from(todayExerciseList);
        const [movedExercise] = reorderedExercises.splice(result.source.index, 1);
        reorderedExercises.splice(result.destination.index, 0, movedExercise);
        setTodayExerciseList(reorderedExercises);

        // repWeight의 순서도 동일하게 변경
        const reorderedRepWeight = Array.from(repWeightList);
        const [movedRep] = reorderedRepWeight.splice(result.source.index, 1);
        reorderedRepWeight.splice(result.destination.index, 0, movedRep);
        setRepWeightList(reorderedRepWeight);

        // 순서가 변경된 데이터를 서버에 동기화
        syncExerciseOrder(reorderedExercises);
    };

    const syncExerciseOrder = async (updatedList) => {
        try {
            const updatedOrder = updatedList.map((exercise, index) => ({
                id: exercise.id,
                sequence: index + 1,
            }));
            const data = await todayService.updateSequence(todayId, updatedOrder);
            console.log(data);
        } catch (error) {
            console.error(error);
        }
    };

    const addSet = (exerciseIndex) => {
        const updatedRepWeight = [...repWeightList];
        updatedRepWeight[exerciseIndex] = [
            ...updatedRepWeight[exerciseIndex],
            { reps: 0, weight: 0 }
        ];
        setRepWeightList(updatedRepWeight);
    };

    const createSavePayload = () => {
        return todayExerciseList.map((exercise, index) => ({
            id: exercise.id,
            repWeightList: repWeightList[index], // 해당 운동의 세트 정보
        }));
    };


    //일지에 운동 추가
    const save = async () => {
        try {
            const payload = createSavePayload();
            console.log("저장할 데이터:", payload);

            // 서버로 POST 요청
            const data = await todayService.updateTodayExercise(todayId, payload);

            onDataChanged();
            console.log("저장 성공:", data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        const fetchToday = async () => {
            try {
                const data = await todayService.findOneTodayById(todayId);
                setToday(data);
                setTodayExerciseList(data.todayExercises);
                setRepWeightList(
                    data.todayExercises.map((exercise) => exercise.repWeightList)
                );
                console.log("검색 결과:", data);
            } catch (error) {
                console.error("루틴 로드 중 오류 발생:", error);
            } finally {
                setIsLoading(false); // 로딩 상태 종료
            }
        };

        fetchToday(); // 비동기 함수 호출
    }, [todayId]);

    useEffect(() => {
        console.log(repWeightList);
    }, [repWeightList]);

    if (isLoading) {
        return <div>로딩 중...</div>; // 로딩 메시지 표시
    }

    if (!today || !today.todayExercises) {
        return <div>오늘의 운동 데이터를 찾을 수 없습니다.</div>; // 데이터 없음 처리
    }

    return (
        <>
            <div className="modal-overlay">
                <div className="modal-content">
                    <div className="button-position">
                        <button className="back-button rotate-right" onClick={() => handleIsEditing(false)}>
                            <IoIosArrowBack />
                        </button>
                    </div>
                    <h3>수정</h3>
                    <DragDropContext onDragEnd={onDragEnd}>
                        <Droppable droppableId="todayExerciseList">
                            {(provided = { innerRef: null, droppableProps: {}, placeholder: null }) => (
                                <div
                                    {...provided.droppableProps}
                                    ref={provided.innerRef}
                                    className="today-exercise-list"
                                >
                                    {todayExerciseList.map((todayExercise, exerciseIndex) => (
                                        <Draggable
                                            key={todayExercise.id}
                                            draggableId={todayExercise.id.toString()}
                                            index={exerciseIndex}
                                        >
                                            {(provided) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    className="routine-exercise-item"
                                                >
                                                    <div className="exercise-header">
                                                        <span>{todayExercise.exerciseName}</span>
                                                        <button
                                                            className="small-text-button"
                                                            onClick={() => addSet(exerciseIndex)}
                                                        >
                                                            세트 추가
                                                        </button>
                                                        <button
                                                            className="small-text-button"
                                                            onClick={() =>
                                                                removeExercise(
                                                                    exerciseIndex,
                                                                    todayExercise.id
                                                                )
                                                            }
                                                        >
                                                            삭제
                                                        </button>
                                                    </div>
                                                    {repWeightList[exerciseIndex]?.map((repWeight, index) => (
                                                        <div className="row" key={index}>
                                                            <div className="number">
                                                                {index + 1}
                                                            </div>
                                                            <input
                                                                className="small-input"
                                                                type="number"
                                                                placeholder={`반복 횟수 (Set ${
                                                                    index + 1
                                                                })`}
                                                                value={repWeight.reps}
                                                                onChange={(e) =>
                                                                    handleSetChange(
                                                                        exerciseIndex,
                                                                        index,
                                                                        "reps",
                                                                        e.target.value
                                                                    )
                                                                }
                                                            />
                                                            <input
                                                                className="small-input"
                                                                type="number"
                                                                placeholder={`무게 (Set ${
                                                                    index + 1
                                                                })`}
                                                                value={repWeight.weight}
                                                                onChange={(e) =>
                                                                    handleSetChange(
                                                                        exerciseIndex,
                                                                        index,
                                                                        "weight",
                                                                        e.target.value
                                                                    )
                                                                }
                                                            />
                                                            <button
                                                                className="small-button"
                                                                onClick={() =>
                                                                    removeSet(exerciseIndex, index)
                                                                }
                                                            >
                                                                <TiDeleteOutline />
                                                            </button>
                                                        </div>
                                                    ))}
                                                </div>
                                            )}
                                        </Draggable>
                                    ))}
                                    {provided.placeholder}
                                </div>
                            )}
                        </Droppable>
                    </DragDropContext>
                    <button onClick={() => save()}>
                        저장
                    </button>
                </div>
            </div>
        </>
    );
};

export default UpdateToday;
