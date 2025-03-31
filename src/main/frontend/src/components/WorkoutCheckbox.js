import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {todayService} from "../services/todayService";

const WorkoutCheckbox =  ({ fetchMonthData, todayId, month, todayData, year }) => {
    const [isChecked, setIsChecked] = useState(false);

    useEffect(() => {
        const fetchCheckboxState = () => {
            try {
                setIsChecked(todayData.checkBox === 'True');
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
            await fetchMonthData(year, month)
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