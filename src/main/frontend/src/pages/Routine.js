import React from 'react';
import "./Page.css";
import AddRoutine from "../components/AddRoutine";
import RoutineList from "../components/RoutineList";

const Routine = () => {
    return (
        <>
            <div className="page">
                <div className="between">
                    <h1>루틴</h1>
                    <AddRoutine/>
                </div>
                <RoutineList/>
            </div>
        </>
    );
};

export default Routine;