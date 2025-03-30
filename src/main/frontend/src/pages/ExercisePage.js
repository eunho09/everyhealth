import React, {useCallback, useEffect, useState} from 'react';
import ExerciseList from '../components/ExerciseList';
import '../styles/ExercisePage.css'
import AddExercise from "../components/AddExercise";
import {exerciseService} from "../services/exerciseService";

const ExercisePage = () => {
    const [exercises, setExercises] = useState([]);
    const [classification, setClassification] = useState({});
    const [isLoading, setIsLoading] = useState(true);

    const fetchExercise =  async() => {
        try {
            const data = await exerciseService.findExerciseByMemberId();
            setExercises(data);
        } catch (error){
            console.error(error);
        }
    };

    const getClassification = async () => {
        try {
            const data = await exerciseService.getClassification();
            setClassification(data);
        } catch (error){
            console.error(error);
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        fetchExercise();
        getClassification();
    }, []);

    if (isLoading){
        return <div></div>
    }

    return (
        <>
            <div className="p-6 max-w-7xl mx-auto">
                {/* 헤더 섹션 */}
                <div className="mb-8">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-3xl font-bold text-gray-800">운동</h1>
                        <AddExercise
                            fetchExercise={fetchExercise}
                            classification={classification}/>
                    </div>

                </div>
                {/* 운동 목록 섹션 */}
                <div className="bg-white rounded-xl shadow-sm p-6">
                    <ExerciseList
                        exercises={exercises}
                        fetchExercise={fetchExercise}
                        classification={classification}/>
                </div>
            </div>
        </>
    );
};

export default ExercisePage;
