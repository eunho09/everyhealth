import React, { useEffect, useState } from "react";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { IoIosArrowBack } from "react-icons/io";
import { TiDeleteOutline } from "react-icons/ti";
import { FaRegTrashAlt } from "react-icons/fa";
import { routineService } from "../services/routineService";

const UpdateRoutine = ({ routines, routineId, editClose, fetchRoutines }) => {
    const [isLoading, setIsLoading] = useState(true);
    const [routineExerciseList, setRoutineExerciseList] = useState([]);

    useEffect(() => {
        const fetchRoutine = () => {
            try {
                const findRoutine = routines.find((routine) => routine.routineId === routineId);
                setRoutineExerciseList(findRoutine.routineExerciseDtoList);
            } catch (error) {
                console.error("일지 로드 중 오류 발생:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchRoutine();
    }, []);

    const handleSetChange = (exerciseIndex, setIndex, field, value) => {
        const updatedList = [...routineExerciseList];
        const numberValue = value ? parseFloat(value) : null;
        console.log("updatedList", updatedList);

        // 특정 운동의 특정 세트를 업데이트
        if (field === 'reps') {
            updatedList[exerciseIndex].repWeightList[setIndex].reps = numberValue;
        } else if (field === 'weight') {
            updatedList[exerciseIndex].repWeightList[setIndex].weight = numberValue;
        }
        setRoutineExerciseList(updatedList);
    };

    const removeSet = (exerciseIndex, setIndex) => {
        const updatedList = [...routineExerciseList];
        updatedList[exerciseIndex].repWeightList = updatedList[exerciseIndex].repWeightList.filter((_, i) => i !== setIndex);
        setRoutineExerciseList(updatedList);
    };

    const handleDeleteExercise = async (exerciseIndex, routineExerciseId) => {
        try {
            await routineService.deleteRoutineExercise(routineExerciseId);
            setRoutineExerciseList(prev => prev.filter((_, i) => i !== exerciseIndex));
            if (routineExerciseList.length <= 0){
                editClose();
            }
            await fetchRoutines();
        } catch (error) {
            console.error(error);
        }
    };

    const onDragEnd = (result) => {
        if (!result.destination) return;
        const reorderedExercises = Array.from(routineExerciseList);
        const [movedExercise] = reorderedExercises.splice(result.source.index, 1);
        reorderedExercises.splice(result.destination.index, 0, movedExercise);
        setRoutineExerciseList(reorderedExercises);
        syncExerciseOrder(reorderedExercises);
    };

    const syncExerciseOrder = async (updatedList) => {
        try {
            const updatedOrder = updatedList.map((exercise, index) => ({
                routineExerciseId: exercise.routineExerciseId,
                sequence: index + 1,
            }));
            await routineService.updateSequence(routineId, updatedOrder);
            await fetchRoutines();
        } catch (error) {
            console.error("순서 업데이트 실패:", error);
        }
    };

    const addSet = (exerciseIndex) => {
        const updatedList = [...routineExerciseList];
        updatedList[exerciseIndex].repWeightList.push({
            reps: 0,
            weight: 0
        });
        setRoutineExerciseList(updatedList);
    };

    const createSavePayload = () => {
        return routineExerciseList.map(re => ({
            routineExerciseId: re.routineExerciseId,
            sequence: re.sequence,
            repWeightList: re.repWeightList
        }));
    };

    const save = async () => {
        try {
            const payload = createSavePayload();
            await routineService.updateRoutineExercise(routineId, payload);
            editClose();
            await fetchRoutines();
        } catch (error) {
            console.error(error);
        }
    };

    if (isLoading) {
        return null;
    }

    if (!routineExerciseList || routineExerciseList.length === 0) {
        editClose();
        return null;
    }

    return (
        <>
            <div className="modal-overlay">
                <div className="modal-content">
                    <div className="button-position">
                        <button className="back-button rotate-right" onClick={() => editClose(false)}>
                            <IoIosArrowBack />
                        </button>
                    </div>
                    <h3>수정</h3>
                    <DragDropContext onDragEnd={onDragEnd}>
                        <Droppable droppableId="todayExerciseList">
                            {(provided) => (
                                <div
                                    {...provided.droppableProps}
                                    ref={provided.innerRef}
                                    className="today-exercise-list"
                                >
                                    {routineExerciseList.map((routineExercise, exerciseIndex) => (
                                        <Draggable
                                            key={routineExercise.routineExerciseId}
                                            draggableId={routineExercise.routineExerciseId.toString()}
                                            index={exerciseIndex}
                                        >
                                            {(provided) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    className="today-exercise-item"
                                                >
                                                    <div className="exercise-header">
                                                        <span>{routineExercise.exerciseName}</span>
                                                        <button
                                                            className="small-text-button"
                                                            onClick={() => addSet(exerciseIndex)}
                                                        >
                                                            세트 추가
                                                        </button>
                                                        <button
                                                            className="small-text-button"
                                                            onClick={() =>
                                                                handleDeleteExercise(
                                                                    exerciseIndex,
                                                                    routineExercise.routineExerciseId
                                                                )
                                                            }
                                                        >
                                                            <FaRegTrashAlt/>
                                                        </button>
                                                    </div>
                                                    {/* 각 운동별 세트 정보 렌더링 */}
                                                    {routineExercise.repWeightList && routineExercise.repWeightList.map((repWeight, setIndex) => (
                                                        <div className="row" key={setIndex}>
                                                            <div className="number">
                                                                {setIndex + 1}
                                                            </div>
                                                            <input
                                                                className="small-input"
                                                                type="number"
                                                                placeholder="반복 횟수"
                                                                value={repWeight.reps}
                                                                onChange={(e) =>
                                                                    handleSetChange(
                                                                        exerciseIndex,
                                                                        setIndex,
                                                                        "reps",
                                                                        e.target.value
                                                                    )
                                                                }
                                                            />
                                                            <input
                                                                className="small-input"
                                                                type="number"
                                                                placeholder="무게"
                                                                value={repWeight.weight}
                                                                onChange={(e) =>
                                                                    handleSetChange(
                                                                        exerciseIndex,
                                                                        setIndex,
                                                                        "weight",
                                                                        e.target.value
                                                                    )
                                                                }
                                                            />
                                                            <button
                                                                className="small-button"
                                                                onClick={() =>
                                                                    removeSet(exerciseIndex, setIndex)
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

export default UpdateRoutine;