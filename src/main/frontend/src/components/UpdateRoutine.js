import React, { useEffect, useState } from "react";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { IoIosArrowBack } from "react-icons/io";
import { TiDeleteOutline } from "react-icons/ti";
import { routineService } from "../services/routineService";

const UpdateRoutine = ({ routineId, handleIsEditing }) => {
    const [isLoading, setIsLoading] = useState(true);
    const [routine, setRoutine] = useState(null);
    const [routineExerciseList, setRoutineExerciseList] = useState([]);

    // handleSetChange 함수 개선
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

    // 세트 제거 함수 개선
    const removeSet = (exerciseIndex, setIndex) => {
        const updatedList = [...routineExerciseList];
        updatedList[exerciseIndex].repWeightList = updatedList[exerciseIndex].repWeightList.filter((_, i) => i !== setIndex);
        setRoutineExerciseList(updatedList);
    };

    // 운동 제거 함수
    const removeExercise = (exerciseIndex, routineExerciseId) => {
        handleDeleteExercise(routineExerciseId);
        setRoutineExerciseList(prev => prev.filter((_, i) => i !== exerciseIndex));
    };

    const handleDeleteExercise = async (routineExerciseId) => {
        try {
            const data = await routineService.deleteRoutineExercise(routineExerciseId);
            console.log(data);
        } catch (error) {
            console.error(error);
        }
    };

    // 드래그 앤 드롭 핸들러 개선
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
                id: exercise.id,
                sequence: index + 1,
            }));
            await routineService.updateSequence(routineId, updatedOrder);
            console.log("순서 업데이트 성공");
        } catch (error) {
            console.error("순서 업데이트 실패:", error);
        }
    };

    // 세트 추가 함수 개선
    const addSet = (exerciseIndex) => {
        const updatedList = [...routineExerciseList];
        updatedList[exerciseIndex].repWeightList.push({
            reps: 0,
            weight: 0
        });
        setRoutineExerciseList(updatedList);
    };

    // 저장 페이로드 생성 개선
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
            console.log("저장할 데이터:", payload);
            const data = await routineService.updateRoutineExercise(routineId, payload);
            handleIsEditing();
            console.log("저장 성공:", data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        const fetchRoutine = async () => {
            try {
                const data = await routineService.findRoutineById(routineId);
                console.log("data", data);
                setRoutine(data);
                setRoutineExerciseList(data);
                console.log("검색 결과:", data);
            } catch (error) {
                console.error("일지 로드 중 오류 발생:", error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchRoutine();
    }, [routineId]);

    if (isLoading) {
        return <div>로딩 중...</div>;
    }

    if (!routineExerciseList || routineExerciseList.length === 0) {
        return <div>일지 데이터를 찾을 수 없습니다.</div>;
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
                                                                removeExercise(
                                                                    exerciseIndex,
                                                                    routineExercise.routineExerciseId
                                                                )
                                                            }
                                                        >
                                                            삭제
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