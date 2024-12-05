import React from 'react';


function Login() {

    return (
        <div className="login-container">
            <div className="login-box">
                <h2>구글 로그인</h2>
                <button className="google-login-button">
                    <a href="http://localhost:8080/oauth2/authorization/google">
                        구글로 로그인
                    </a>
                </button>
            </div>
        </div>
    );
}

export default Login;
