import React from 'react';
import "./Page.css";
import AddRoutine from "../components/AddRoutine";
import RoutineManager from "../components/feature/RoutineManager";

const Routine = () => {
    return (
        <>
            <div className="page">
                <div className="between">
                    <h1>루틴</h1>
                    <AddRoutine/>
                </div>
                <RoutineManager/>
            </div>
        </>
    );
};

export default Routine;