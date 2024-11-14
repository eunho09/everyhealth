// ExercisePage.js
import React from 'react';
import ExerciseList from '../components/ExerciseList';
import './Page.css'
import AddExercise from "../components/AddExercise";

const ExercisePage = () => {
    return (
        <>
            <div className="page">
                <div className="between">
                    <h1>운동</h1>
                    <AddExercise/>
                </div>
                <ExerciseList />
            </div>
        </>
    );
};

export default ExercisePage;
