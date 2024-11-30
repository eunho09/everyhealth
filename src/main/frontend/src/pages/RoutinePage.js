import React from 'react';
import "../styles/Page.css";
import AddRoutine from "../components/AddRoutine";
import RoutineManager from "../components/feature/RoutineManager";

const RoutinePage = () => {
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

export default RoutinePage;