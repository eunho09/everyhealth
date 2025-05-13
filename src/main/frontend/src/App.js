// App.js
import React, { useMemo } from 'react';
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

function AppWithRouter() {
  const { user, isLoading } = useAuth();

  const requireAuthToLogin = () => {
    if (isLoading) {
      return null;
    }

    if (!user) {
      return redirect('/login');
    }
    return null;
  };

  const requireAuthToRoot = () => {
    if (isLoading) {
      return null;
    }

    if (user) {
      return redirect('/');
    }
    return null;
  };


  const router = useMemo(() => {
    return createBrowserRobuter([
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
    ]);
  }, [user, isLoading]);

  if (isLoading) {
    return <div>Loading...</div>;
  }


  return <RouterProvider router={router} />;
}

function App() {
  return (
      <AuthProvider>
        <AppWithRouter />
      </AuthProvider>
  );
}

export default App;