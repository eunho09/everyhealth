import React from "react";
import './Sidebar.css';
import logo from './img.png';
import {Link} from "react-router-dom";

const Sidebar = () => {
    return (
        <div className="sidebar">
            <img src={logo}></img>
            <ul>
                <li><Link to="/">홈</Link></li>
                <li>운동일지</li>
                <li>포스트</li>
                <li><Link to="/exercise">운동</Link></li>
                <li><Link to="/routine">루틴</Link></li>
            </ul>
        </div>
    )
}

export default Sidebar;

