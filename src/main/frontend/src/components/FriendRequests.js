// FriendRequests.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaCheck, FaTimes, FaUserFriends, FaUserPlus } from 'react-icons/fa';
import '../styles/FriendRequests.css';

const FriendRequests = () => {
    const [friendRequests, setFriendRequests] = useState([]);
    const [suggestedFriends, setSuggestedFriends] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        Promise.all([
            fetchFriendRequests(),
            fetchSuggestedFriends()
        ]).finally(() => setLoading(false));
    }, []);

    const fetchFriendRequests = async () => {
        try {
            const response = await axios.get('/api/member/friend/request');
            setFriendRequests(response.data);
            console.log(response.data);
        } catch (err) {
            setError('친구 요청을 불러오는데 실패했습니다.');
            console.error('친구 요청 로드 실패:', err);
        }
    };

    const fetchSuggestedFriends = async () => {
        try {
            // API 엔드포인트는 백엔드 구현에 맞게 수정 필요
            const response = await axios.get('/api/member/suggested-friends');
            setSuggestedFriends(Array.isArray(response.data) ? response.data : []);
            console.log(response.data);
        } catch (err) {
            setSuggestedFriends([]); // 에러 시 빈 배열로 설정
            console.error('추천 친구 로드 실패:', err);
        }
    };

    const handleAccept = async (friendId) => {
        try {
            await axios.post(`/api/friend/accept/${friendId}`);
            setFriendRequests(prev => prev.filter(request => request.member.id !== friendId));
            alert('친구 요청을 수락했습니다.');
        } catch (err) {
            console.error('친구 수락 실패:', err);
            alert('친구 수락에 실패했습니다.');
        }
    };

    const handleReject = async (friendId) => {
        try {
            await axios.post(`/api/friend/cancel/${friendId}`);
            setFriendRequests(prev => prev.filter(request => request.friend.id !== friendId));
            alert('친구 요청을 거절했습니다.');
        } catch (err) {
            console.error('친구 거절 실패:', err);
            alert('친구 거절에 실패했습니다.');
        }
    };

    const handleSendRequest = async (friendId) => {
        try {
            await axios.post(`/api/friend/request/${friendId}`);
            // 요청 성공 후 추천 목록에서 제거
            setSuggestedFriends(prev => prev.filter(friend => friend.id !== friendId));
            alert('친구 요청을 보냈습니다.');
        } catch (err) {
            console.error('친구 요청 실패:', err);
            alert('친구 요청에 실패했습니다.');
        }
    };

    if (loading) {
        return <div className="friend-requests-loading">Loading...</div>;
    }

    return (
        <div className="friend-requests-container">
            {/* 친구 요청 섹션 */}
            <section className="friend-section">
                <div className="friend-section-header">
                    <FaUserFriends className="friend-icon" />
                    <h2>친구 요청</h2>
                </div>

                {friendRequests.length === 0 ? (
                    <div className="no-requests">
                        <p>새로운 친구 요청이 없습니다.</p>
                    </div>
                ) : (
                    <div className="friend-list">
                        {friendRequests.map((request) => (
                            <div key={request.id} className="friend-item request-item">
                                <div className="friend-info">
                                    <div className="friend-avatar">
                                        {request.member.picture ? (
                                            <img src={request.member.picture} alt={request.member.name} />
                                        ) : (
                                            <div className="avatar-placeholder">
                                                {request.member.name.charAt(0)}
                                            </div>
                                        )}
                                    </div>
                                    <div className="friend-details">
                                        <h3>{request.member.name}</h3>
                                    </div>
                                </div>
                                <div className="friend-actions">
                                    <button
                                        className="action-button accept"
                                        onClick={() => handleAccept(request.member.id)}
                                    >
                                        <FaCheck />
                                        수락
                                    </button>
                                    <button
                                        className="action-button reject"
                                        onClick={() => handleReject(request.member.id)}
                                    >
                                        <FaTimes />
                                        거절
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>

            {/* 추천 친구 섹션 */}
            <section className="friend-section">
                <div className="friend-section-header">
                    <FaUserPlus className="friend-icon" />
                    <h2>추천 친구</h2>
                </div>

                {suggestedFriends.length === 0 ? (
                    <div className="no-requests">
                        <p>추천할 친구가 없습니다.</p>
                    </div>
                ) : (
                    <div className="friend-list suggested-list">
                        {suggestedFriends.map((friend) => (
                            <div key={friend.id} className="friend-item suggested-item">
                                <div className="friend-info">
                                    <div className="friend-avatar">
                                        {friend.picture ? (
                                            <img src={friend.picture} alt={friend.name} />
                                        ) : (
                                            <div className="avatar-placeholder">
                                                {friend.name.charAt(0)}
                                            </div>
                                        )}
                                    </div>
                                    <div className="friend-details">
                                        <h3>{friend.name}</h3>
                                    </div>
                                </div>
                                <button
                                    className="action-button request"
                                    onClick={() => handleSendRequest(friend.id)}
                                >
                                    <FaUserPlus />
                                    친구 요청
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
};

export default FriendRequests;