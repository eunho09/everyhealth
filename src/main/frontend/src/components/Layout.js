/*
import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "../styles/Layout.css";

const Layout = () => {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const handleResize = () => {
            setIsMobile(window.innerWidth <= 768); // 768px 이하를 모바일로 판단
        };
        handleResize(); // 초기 실행
        window.addEventListener("resize", handleResize); // 창 크기 변경 이벤트
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    return (
        <div className="layout">
            {isMobile ? (
                <>
                    <header className="header">
                        <h1>운동 관리 시스템</h1>
                    </header>
                    <main className="content">
                        {/!* 메인 컨텐츠 *!/}
                    </main>
                    <footer className="footer">
                        <nav className="bottom-nav">
                            <Link to="/">홈</Link>
                            <Link to="/exercise">운동</Link>
                            <Link to="/routine">루틴</Link>
                        </nav>
                    </footer>
                </>
            ) : (
                <div className="main">
                    <aside className="sidebar">
                        {/!* 사이드바 내용 *!/}
                        <ul>
                            <li><Link to="/">홈</Link></li>
                            <li><Link to="/journal">운동일지</Link></li>
                            <li><Link to="/post">포스트</Link></li>
                            <li><Link to="/exercise">운동</Link></li>
                            <li><Link to="/routine">루틴</Link></li>
                        </ul>
                    </aside>
                    <main className="content">
                        {/!* 메인 컨텐츠 *!/}
                    </main>
                </div>
            )}
        </div>
    );
};

export default Layout;
*/
