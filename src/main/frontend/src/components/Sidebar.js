import React from "react";
import '../styles/Sidebar.css';
import logo from '../assets/logo.png';
import {Link} from "react-router-dom";

const Sidebar = () => {
    return (
        <div className="sidebar">
            <img src={logo}></img>
            <ul>
                <li><Link to="/">홈</Link></li>
                <li>운동일지</li>
                <li><Link to="/post">포스트</Link></li>
                <li><Link to="/exercise">운동</Link></li>
                <li><Link to="/routine">루틴</Link></li>
            </ul>
            <a href="http://localhost:8080/oauth2/authorization/google">로그인</a>
        </div>
    )
}

export default Sidebar;

