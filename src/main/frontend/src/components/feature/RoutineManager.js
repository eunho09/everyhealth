import React, {useState} from 'react';
import RoutineList from '../RoutineList';
import AddRoutineExercise from '../AddRoutineExercise';
import UpdateRoutine from "../UpdateRoutine";

const RoutineManager = ({routines, fetchRoutines}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRoutineId, setSelectedRoutineId] = useState(null);

    const [isEditing, setIsEditing] = useState(false);


    const openModal = (routineId) => {
        setSelectedRoutineId(routineId);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };


    const editingOpen = (routineId) => {
        setSelectedRoutineId(routineId);
        setIsEditing(true);
    }

    const editingClose = () => {
        setIsEditing(false);
    }

    return (
        <div>
            <h2>루틴 목록</h2>
            <RoutineList
                routines={routines}
                open={openModal}
                edit={editingOpen}
                fetchRoutines={fetchRoutines}
            />
            {isModalOpen && (
                <AddRoutineExercise
                    routines={routines}
                    routineId={selectedRoutineId}
                    fetchRoutines={fetchRoutines}
                    onClose={closeModal}
                />
            )}
            {isEditing && (
                <UpdateRoutine
                    routines={routines}
                    routineId={selectedRoutineId}
                    fetchRoutines={fetchRoutines}
                    editClose={editingClose}
                />
            )}
        </div>
    );
};

export default RoutineManager;
