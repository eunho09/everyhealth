import React, {useEffect, useState} from 'react';
import {memberService} from "../services/memberService";
import {postService} from "../services/postService";
import {FaRegImages} from "react-icons/fa";
import SharedCalendarLogManager from "./SharedCalendarLogManager";

const FriendProfile = ({friendId}) => {
    const [activeTab, setActiveTab] = useState('posts');
    const [friendProfile, setFriendProfile] = useState(null);
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchFriendData = async () => {
            try {
                // 친구 정보와 친구의 게시물 가져오기
                const [profileRes, postsRes] = await Promise.all([
                    memberService.findFriendById(friendId),
                    postService.findFriendPosts(friendId)
                ]);

                setFriendProfile(profileRes);
                setPosts(postsRes);
            } catch (error) {
                console.error('친구 데이터 로드 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchFriendData();
    }, [friendId]);

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <div className="profile-container">
            {/* 프로필 헤더 */}
            <div className="profile-header">
                <div className="profile-avatar">
                    {friendProfile?.picture ? (
                        <img src={friendProfile.picture} alt="프로필" />
                    ) : (
                        <div className="profile-avatar-placeholder">
                            {friendProfile?.name?.charAt(0)}
                        </div>
                    )}
                </div>
                <div className="profile-infos">
                    <h1 className="profile-name">{friendProfile?.name}</h1>
                    <div className="profile-stats">
                        <div className="stat-item">
                            <span className="stat-value">{posts.length}</span>
                            <span className="stat-label">게시물</span>
                        </div>
                    </div>
                </div>
            </div>

            {/* 탭 메뉴 */}
            <div className="profile-tabs">
                <button
                    className={`tab-button ${activeTab === 'posts' ? 'active' : ''}`}
                    onClick={() => setActiveTab('posts')}
                >
                    <FaRegImages/>
                    <span>게시물</span>
                </button>
                <button
                    className={`tab-button ${activeTab === 'calendar' ? 'active' : ''}`}
                    onClick={() => setActiveTab('calendar')}
                >
                    <span>운동 캘린더</span>
                </button>
            </div>

            {/* 컨텐츠 영역 */}
            <div className="profile-content">
                {activeTab === 'calendar' ? (
                    <div className="calendar-container">
                        <SharedCalendarLogManager friendId={friendId} />
                    </div>
                ) : (
                    <div className="posts-grid">
                        {posts.map(post => (
                            <div key={post.id} className="post-item">
                                <img src={`/api/images/${post.imageUrl}`} alt={post.text} />
                                <div className="post-overlay">
                                    <p className="post-text">{post.text}</p>
                                </div>
                            </div>
                        ))}
                        {posts.length === 0 && (
                            <p className="no-content">아직 게시물이 없습니다.</p>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default FriendProfile;