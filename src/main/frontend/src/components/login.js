import React from 'react';

const Login = () => {

    return (
        <div>
            <h1>Google Login</h1>
            <button>
                <a href="/oauth2/authorization/google">로그인</a>
            </button>
        </div>
    );
};

export default Login;