import React, {useEffect, useState} from "react";
import '../styles/Sidebar.css';
import logo from '../assets/logo.png';
import {Link} from "react-router-dom";
import {loginCheck} from "../api/LoginApi";

const Sidebar = () => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const response = await loginCheck();
                if (response.check) {
                    setUser(response);
                }
            } catch (error) {
                console.error('로그인 상태 확인 실패:', error);
            }
        };

        checkLoginStatus();
    }, []);


    return (
        <div className="sidebar">
            <div className="logo-container">
                <img src={logo}></img>
            </div>
            <nav className="sidebar-nav">
                <ul>
                    <li><Link to="/"><span>홈</span></Link></li>
                    <li><Link to="/post"><span>포스트</span></Link></li>
                    <li><Link to="/exercise"><span>운동</span></Link></li>
                    <li><Link to="/routine"><span>루틴</span></Link></li>
                </ul>
            </nav>

            <div className="auth-container">
                {user ? (
                    <div className="profile-section">
                        <Link to="/profile">
                            <div className="profile-info">
                                <img
                                    src={user.picture}
                                    alt="프로필"
                                    className="profile-image"
                                />
                                <span className="user-name">{user.name}</span>
                            </div>
                        </Link>
                    </div>
                ) : (
                    <Link to="/login" className="login-button">
                        <span>로그인</span>
                    </Link>
                )}
            </div>
        </div>
    );
};

export default Sidebar;

