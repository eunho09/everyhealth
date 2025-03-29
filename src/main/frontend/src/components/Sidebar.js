import React, {useEffect, useState} from "react";
import '../styles/Sidebar.css';
import logo from '../assets/logo.png';
import {Link, useNavigate, useLocation} from "react-router-dom";
import {useAuth} from "../context/AuthContext";

const Sidebar = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const {user, logout} = useAuth();


    const isLoggedIn = !!user;

    useEffect(() => {
        console.log("isLoggedIn : ", isLoggedIn);

        if (isLoggedIn && location.pathname === "/login") {
            console.log("실행");
            navigate("/");
        }
    }, [isLoggedIn, location.pathname, navigate]);

    const handleLogout = async() => {
        const success = await logout();
        if (success) {
            navigate("/login");
        }
    }

    return (
        <div className="sidebar">
            <div className="logo-container">
                <img src={logo} alt="Logo" />
            </div>
            <nav className="sidebar-nav">
                <ul>
                    <li><Link to="/"><span>홈</span></Link></li>
                    <li><Link to="/post"><span>포스트</span></Link></li>
                    <li><Link to="/exercise"><span>운동</span></Link></li>
                    <li><Link to="/routine"><span>루틴</span></Link></li>
                    <li><Link to="/club"><span>클럽</span></Link></li>
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
                        <button onClick={handleLogout} className="login-button">로그아웃</button>
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