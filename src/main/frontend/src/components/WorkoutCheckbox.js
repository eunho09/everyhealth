import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {todayService} from "../services/todayService";

const WorkoutCheckbox =  ({onDataChanged, todayId }) => {
    const [isChecked, setIsChecked] = useState(false);

    // Load initial checkbox state
    useEffect(() => {
        const fetchCheckboxState = async () => {
            try {
                const data = await todayService.findOneTodayById(todayId);
                setIsChecked(data.checkBox === 'True');
            } catch (error) {
                console.error('Error fetching checkbox state:', error);
            }
        };

        fetchCheckboxState();
    }, [todayId]);

    const handleCheckboxChange = async (event) => {
        const checked = event.target.checked;
        setIsChecked(checked);

        try {
            await todayService.updateCheckbox(todayId, checked);

            onDataChanged();
        } catch (error) {
            console.error('Error updating checkbox:', error);
            setIsChecked(!checked);
        }
    };

    return (
        <div className="flex items-center">
            <input
                type="checkbox"
                checked={isChecked}
                onChange={handleCheckboxChange}
                className="w-4 h-4 text-blue-600 rounded focus:ring-blue-500"
                aria-label="완료 체크박스"
            />
            <label className="ml-2 text-sm text-gray-700">
                완료
            </label>
        </div>
    );
};

export default WorkoutCheckbox;