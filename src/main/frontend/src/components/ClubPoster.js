import React from 'react';
import { Calendar, MapPin, Trophy, MessageCircle } from 'lucide-react';
import '../styles/ClubPoster.css';
import {useNavigate} from "react-router-dom";
import axios from "axios";

const ClubPoster = ({clubData}) => {
    const navigate = useNavigate();

    const fetchJoinClub = async () => {
        try {
            const response = await axios.post(`/api/club/${clubData.id}/join`);
        } catch (error){
            console.log(error);
        }
    }

    const handleChatClick = (chatRoomId) => {
        navigate(`/chat/${chatRoomId}`);
        fetchJoinClub();
        console.log(`${chatRoomId}번 채팅방으로 이동`);
    };

    return (
        <div className="poster-container">
            <div className="poster-card">
                {/* 메인 비주얼 */}
                <div className="poster-header">
                    <h1 className="poster-title">{clubData.title}</h1>
                    <p className="poster-description">{clubData.content}</p>

                    {/* 주요 정보 아이콘 */}
                    <div className="info-grid">
                        <div className="info-item">
                            <MapPin className="icon" />
                            <span className="info-text">{clubData.location}</span>
                        </div>
                        <div className="info-item">
                            <Calendar className="icon" />
                            <span className="info-text">{clubData.schedule}</span>
                        </div>
                    </div>
                </div>

                {/* 컨텐츠 영역 */}
                <div className="poster-content">
                    {/* 클럽 특징 */}
                    <div className="features-section">
                        <h2 className="features-title">
                            <Trophy className="icon" />
                            클럽 특징
                        </h2>
                        <div className="feature-list">
                            {clubData.highlights.map((highlight, index) => (
                                <div key={index} className="feature-item">
                                    <div className="feature-bullet"></div>
                                    <span>{highlight}</span>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* 채팅 버튼 */}
                    <button
                        onClick={() => handleChatClick(clubData.chatRoomId)}
                        className="chat-button"
                    >
                        <MessageCircle className="icon" />
                        채팅방 입장하기
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ClubPoster;