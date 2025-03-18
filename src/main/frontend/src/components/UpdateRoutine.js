import React, { useEffect, useState } from "react";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { IoIosArrowBack } from "react-icons/io";
import { TiDeleteOutline } from "react-icons/ti";
import { todayService } from "../services/todayService";

const UpdateToday = ({ onDataChanged, todayId, handleIsEditing }) => {
    const [isLoading, setIsLoading] = useState(true);
    const [today, setToday] = useState(null);
    const [todayExerciseList, setTodayExerciseList] = useState([]);

    // handleSetChange 함수 개선
    const handleSetChange = (exerciseIndex, setIndex, field, value) => {
        const updatedList = [...todayExerciseList];
        const numberValue = value ? parseFloat(value) : 0;
        console.log("updatedList", updatedList);

        // 특정 운동의 특정 세트를 업데이트
        if (field === 'reps') {
            updatedList[exerciseIndex].repWeightList[setIndex].reps = numberValue;
        } else if (field === 'weight') {
            updatedList[exerciseIndex].repWeightList[setIndex].weight = numberValue;
        }
        setTodayExerciseList(updatedList);
    };

    // 세트 제거 함수 개선
    const removeSet = (exerciseIndex, setIndex) => {
        const updatedList = [...todayExerciseList];
        updatedList[exerciseIndex].repWeightList = updatedList[exerciseIndex].repWeightList.filter((_, i) => i !== setIndex);
        setTodayExerciseList(updatedList);
    };

    // 운동 제거 함수
    const removeExercise = (exerciseIndex, todayExerciseId) => {
        handleDeleteExercise(todayExerciseId);
        setTodayExerciseList(prev => prev.filter((_, i) => i !== exerciseIndex));
    };

    const handleDeleteExercise = async (todayExerciseId) => {
        try {
            const data = await todayService.deleteTodayExercise(todayExerciseId);
            console.log(data);
        } catch (error) {
            console.error(error);
        }
    };

    // 드래그 앤 드롭 핸들러 개선
    const onDragEnd = (result) => {
        if (!result.destination) return;
        const reorderedExercises = Array.from(todayExerciseList);
        const [movedExercise] = reorderedExercises.splice(result.source.index, 1);
        reorderedExercises.splice(result.destination.index, 0, movedExercise);
        setTodayExerciseList(reorderedExercises);
        syncExerciseOrder(reorderedExercises);
    };

    const syncExerciseOrder = async (updatedList) => {
        try {
            const updatedOrder = updatedList.map((exercise, index) => ({
                id: exercise.id,
                sequence: index + 1,
            }));
            await todayService.updateSequence(todayId, updatedOrder);
            console.log("순서 업데이트 성공");
        } catch (error) {
            console.error("순서 업데이트 실패:", error);
        }
    };

    // 세트 추가 함수 개선
    const addSet = (exerciseIndex) => {
        const updatedList = [...todayExerciseList];
        updatedList[exerciseIndex].repWeightList.push({
            reps: 0,
            weight: 0
        });
        setTodayExerciseList(updatedList);
    };

    // 저장 페이로드 생성 개선
    const createSavePayload = () => {
        return todayExerciseList.map(exercise => ({
            id: exercise.id,
            repWeightList: exercise.repWeightList
        }));
    };

    const save = async () => {
        try {
            const payload = createSavePayload();
            console.log("저장할 데이터:", payload);
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
                console.log("검색 결과:", data);
            } catch (error) {
                console.error("일지 로드 중 오류 발생:", error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchToday();
    }, [todayId]);

    if (isLoading) {
        return <div>로딩 중...</div>;
    }

    if (!today || !today.todayExercises || today.todayExercises.length === 0) {
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
                                                    className="today-exercise-item"
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
                                                    {/* 각 운동별 세트 정보 렌더링 */}
                                                    {todayExercise.repWeightList && todayExercise.repWeightList.map((repWeight, setIndex) => (
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

export default UpdateToday;