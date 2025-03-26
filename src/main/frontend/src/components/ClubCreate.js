import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/ClubCreate.css';
import {clubService} from "../services/ClubService";

const ClubCreate = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        content: '',
        location: '',
        schedule: '',
        highlight: ''
    });
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        if (!formData.title.trim()) newErrors.title = '클럽 이름을 입력해주세요.';
        if (!formData.content.trim()) newErrors.content = '클럽 소개를 입력해주세요.';
        if (!formData.location.trim()) newErrors.location = '활동 장소를 입력해주세요.';
        if (!formData.schedule.trim()) newErrors.schedule = '활동 시간을 입력해주세요.';
        if (!formData.highlight.trim()) newErrors.highlight = '활동 시간을 입력해주세요.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            const data = clubService.save(formData);
            navigate('/club');
        } catch (error) {
            console.error('Error creating club:', error);
            setErrors({ submit: '클럽 생성 중 오류가 발생했습니다.' });
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        // 에러 메시지 제거
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    return (
        <div className="create-container">
            <div className="create-header">
                <h1 className="create-title">새로운 클럽 만들기</h1>
                <p className="create-subtitle">새로운 클럽을 만들고 멤버들과 함께하세요.</p>
            </div>

            <form className="create-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label className="form-label">클럽 이름</label>
                    <input
                        type="text"
                        name="title"
                        className="form-input"
                        value={formData.title}
                        onChange={handleChange}
                        placeholder="클럽 이름을 입력하세요"
                    />
                    {errors.title && <p className="error-message">{errors.title}</p>}
                </div>

                <div className="form-group">
                    <label className="form-label">클럽 소개</label>
                    <textarea
                        name="content"
                        className="form-textarea"
                        value={formData.content}
                        onChange={handleChange}
                        placeholder="클럽을 소개해주세요"
                    />
                    {errors.content && <p className="error-message">{errors.content}</p>}
                </div>

                <div className="form-group">
                    <label className="form-label">활동 장소</label>
                    <input
                        type="text"
                        name="location"
                        className="form-input"
                        value={formData.location}
                        onChange={handleChange}
                        placeholder="주 활동 장소를 입력하세요"
                    />
                    {errors.location && <p className="error-message">{errors.location}</p>}
                </div>

                <div className="form-group">
                    <label className="form-label">활동 시간</label>
                    <input
                        type="text"
                        name="schedule"
                        className="form-input"
                        value={formData.schedule}
                        onChange={handleChange}
                        placeholder="예: 매주 화요일, 토요일 저녁 7시"
                    />
                    {errors.schedule && <p className="error-message">{errors.schedule}</p>}
                </div>

                <div className="form-group">
                    <label className="form-label">클럽 특징</label>
                    <input
                        type="text"
                        className="form-input"
                        name="highlight"
                        onChange={handleChange}
                        placeholder={`특징`}
                    />
                    {errors.highlight && <p className="error-message">{errors.highlight}</p>}
                </div>

                {errors.submit && <p className="error-message">{errors.submit}</p>}

                <div className="form-actions">
                    <button type="submit" className="submit-button">
                        클럽 만들기
                    </button>
                    <button
                        type="button"
                        className="cancel-button"
                        onClick={() => navigate('/club')}
                    >
                        취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ClubCreate;