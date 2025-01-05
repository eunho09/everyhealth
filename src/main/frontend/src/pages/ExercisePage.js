// ExercisePage.js
import React from 'react';
import ExerciseList from '../components/ExerciseList';
import '../styles/ExercisePage.css'
import AddExercise from "../components/AddExercise";

const ExercisePage = () => {
    return (
        <>
            <div className="p-6 max-w-7xl mx-auto">
                {/* 헤더 섹션 */}
                <div className="mb-8">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-3xl font-bold text-gray-800">운동</h1>
                        <AddExercise/>
                    </div>

                </div>
                {/* 운동 목록 섹션 */}
                <div className="bg-white rounded-xl shadow-sm p-6">
                    <ExerciseList />
                </div>
            </div>
        </>
    );
};

export default ExercisePage;
