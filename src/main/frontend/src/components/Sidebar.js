import React, {useEffect, useState} from "react";
import '../styles/Sidebar.css';
import logo from '../assets/logo.png';
import {Link, useNavigate, useLocation} from "react-router-dom";
import {loginService} from "../services/loginService";
import {setAuthState} from "../services/api";

const Sidebar = () => {
    const [user, setUser] = useState(null);
    const [isLogin, setIsLogin] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();

    // Use a ref to track if this is the first render
    const isInitialMount = React.useRef(true);

    useEffect(() => {
        // Create a flag in sessionStorage to prevent duplicate login checks
        // This helps across page reloads too
        const loginCheckFlag = sessionStorage.getItem('loginCheckInProgress');

        const checkLoginStatus = async () => {
            // Skip if a check is already in progress
            if (loginCheckFlag === 'true') {
                return;
            }

            try {
                sessionStorage.setItem('loginCheckInProgress', 'true');
                setIsLoading(true);

                const data = await loginService.loginCheck();
                if (data.check) {
                    setUser(data);
                    setIsLogin(true);
                    setAuthState(true); // 전역 상태 설정
                    console.log("로그인 성공");

                    // Only redirect if we're on the login page and already logged in
                    if (location.pathname === "/login") {
                        navigate("/");
                    }
                }
            } catch (error) {
                console.error('로그인 상태 확인 실패:', error);
            } finally {
                setIsLoading(false);
                // Remove the flag after checking is complete
                sessionStorage.removeItem('loginCheckInProgress');
            }
        };

        // If this is the initial mount or the path changes to /login, check login status
        if (isInitialMount.current || location.pathname === "/login") {
            isInitialMount.current = false;
            checkLoginStatus();
        }
    }, [location.pathname, navigate]);

    const handleLogout = async() => {
        try {
            await loginService.logout();
            setUser(null);
            setIsLogin(false);
            setAuthState(false); // 전역 상태 설정
            navigate("/login");
        } catch (error){
            console.error(error);
        }
    }

    // We don't need the second useEffect for navigation anymore

    if (isLoading) {
        return <div className="sidebar">Loading...</div>;
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