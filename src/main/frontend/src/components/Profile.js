// ProfilePage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/Profile.css';
import { FaUserFriends, FaRegImages, FaUserPlus } from 'react-icons/fa';
import {Link} from "react-router-dom";

const ProfilePage = () => {
    const [activeTab, setActiveTab] = useState('posts');
    const [profile, setProfile] = useState(null);
    const [posts, setPosts] = useState([]);
    const [friends, setFriends] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const [profileRes, postsRes, friendsRes] = await Promise.all([
                    axios.get('/api/login/check'),
                    axios.get('/api/member/posts'),
                    axios.get('/api/member/friend')
                ]);

                setProfile(profileRes.data);
                setPosts(postsRes.data);
                setFriends(friendsRes.data);
                console.log(friendsRes.data);
            } catch (error) {
                console.error('데이터 로드 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchProfileData();
    }, []);

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <div className="profile-container">
            {/* 프로필 헤더 */}
            <div className="profile-header">
                <div className="profile-avatar">
                    {profile?.picture ? (
                        <img src={profile.picture} alt="프로필" />
                    ) : (
                        <div className="profile-avatar-placeholder">
                            {profile?.name?.charAt(0)}
                        </div>
                    )}
                </div>
                <div className="profile-infos">
                    <h1 className="profile-name">{profile?.name}</h1>
                    <div className="profile-stats">
                        <div className="stat-item">
                            <span className="stat-value">{posts.length}</span>
                            <span className="stat-label">게시물</span>
                        </div>
                        <div className="stat-item">
                            <span className="stat-value">{friends.length}</span>
                            <span className="stat-label">친구</span>
                        </div>
                        <div className="">
                            <Link to="/friend">
                                <FaUserPlus />
                            </Link>
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
                    <FaRegImages />
                    <span>게시물</span>
                </button>
                <button
                    className={`tab-button ${activeTab === 'friends' ? 'active' : ''}`}
                    onClick={() => setActiveTab('friends')}
                >
                    <FaUserFriends />
                    <span>친구</span>
                </button>
            </div>

            {/* 컨텐츠 영역 */}
            <div className="profile-content">
                {activeTab === 'posts' ? (
                    <div className="posts-grid">
                        {posts.map(post => (
                            <div key={post.id} className="post-item">
                                <img src={post.imageUrl} alt={post.text} />
                                <div className="post-overlay">
                                    <p className="post-text">{post.text}</p>
                                </div>
                            </div>
                        ))}
                        {posts.length === 0 && (
                            <p className="no-content">아직 게시물이 없습니다.</p>
                        )}
                    </div>
                ) : (
                    <div className="friends-list">
                        {friends.map(friend => (
                            <div key={friend.id} className="friend-item">
                                <div className="friend-avatar">
                                    {friend.friend.picture ? (
                                        <img src={friend.friend.picture} alt={friend.friend.name} />
                                    ) : (
                                        <div className="friend-avatar-placeholder">
                                            {friend.friend.name.charAt(0)}
                                        </div>
                                    )}
                                </div>
                                <div className="friend-info">
                                    <h3 className="friend-name">{friend.friend.name}</h3>
                                </div>
                            </div>
                        ))}
                        {friends.length === 0 && (
                            <p className="no-content">아직 친구가 없습니다.</p>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;