import React from 'react';
import { createBrowserRouter, RouterProvider, redirect } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import CalenderLogPage from './pages/CalenderLogPage';
import PostPage from './pages/PostPage';
import ExercisePage from './pages/ExercisePage';
import LoginPage from './pages/LoginPage';
import './App.css';
import Root from "./pages/Root";
import RoutinePage from "./pages/RoutinePage";
import PostUpload from "./components/PostUpload";
import ProfilePage from "./pages/ProfilePage";
import FriendRequestPage from "./pages/FriendRequestPage";
import FriendProfilePage from "./pages/FriendProfilePage";
import ClubPage from "./pages/ClubPage";
import ClubCreate from "./components/ClubCreate";
import ChatRoom from "./components/ChatRoom";

export const requireAuthToLogin = async () => {
  const isAuthenticated = localStorage.getItem('auth_state') === 'true';
  if (!isAuthenticated) {
    return redirect('/login');
  }
  return null;
};

export const requireAuthToRoot = async () => {
  const isAuthenticated = localStorage.getItem('auth_state') === 'true';
  if (isAuthenticated) {
    return redirect('/');
  }
  return null;
};

const createRoutes = () => [
  {
    path: '/',
    element: <Root />,
    children: [
      {
        index: true,
        element: <CalenderLogPage />,
        loader: requireAuthToLogin
      },
      {
        path: '/post',
        element: <PostPage />,
        loader: requireAuthToLogin
      },
      {
        path: '/post/upload',
        element: <PostUpload />,
        loader: requireAuthToLogin
      },
      {
        path: '/exercise',
        element: <ExercisePage />,
        loader: requireAuthToLogin
      },
      {
        path: '/routine',
        element: <RoutinePage />,
        loader: requireAuthToLogin
      },
      {
        path: '/profile',
        element: <ProfilePage />,
        loader: requireAuthToLogin
      },
      {
        path: '/friend',
        element: <FriendRequestPage />,
        loader: requireAuthToLogin
      },
      {
        path: '/friendProfile/:friendId',
        element: <FriendProfilePage />,
        loader: requireAuthToLogin
      },
      {
        path: '/club',
        element: <ClubPage />,
        loader: requireAuthToLogin
      },
      {
        path: '/club/create',
        element: <ClubCreate />,
        loader: requireAuthToLogin
      },
      {
        path: '/chat/:roomId',
        element: <ChatRoom />,
        loader: requireAuthToLogin
      },
      {
        path: '/login',
        element: <LoginPage />,
        loader: requireAuthToRoot
      }
    ]
  }
];


function App() {

  const router = createBrowserRouter(createRoutes());

  return (
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
  );
}

export default App;