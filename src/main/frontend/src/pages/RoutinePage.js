import React, {useEffect, useState} from 'react';
import "../styles/Page.css";
import AddRoutine from "../components/AddRoutine";
import RoutineManager from "../components/feature/RoutineManager";
import {routineService} from "../services/routineService";

const RoutinePage = () => {
    const [routines, setRoutines] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const fetchRoutineData = async () => {
        try {
            const data = await routineService.findRoutineByMemberId();
            setRoutines(data);
            setIsLoading(false);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchRoutineData()
    }, []);

    if (isLoading){
        return <div></div>
    }

        return (
            <>
            <div className="page">
                <div className="between">
                    <h1>루틴</h1>
                    <AddRoutine
                    fetchRoutines={fetchRoutineData}
                    />
                </div>
                <RoutineManager
                routines={routines}
                fetchRoutines={fetchRoutineData}
                />
            </div>
        </>
    );
};

export default RoutinePage;