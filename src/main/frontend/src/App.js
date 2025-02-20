import React from "react";
import './App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Root from "./pages/Root";
import ErrorPage from "./pages/ErrorPage";
import CalenderLogPage from "./pages/CalenderLogPage";
import RoutinePage from "./pages/RoutinePage";
import ExercisePage from "./pages/ExercisePage";
import PostPage from "./pages/PostPage";
import PostUpload from "./components/PostUpload";
import LoginPage from "./pages/LoginPage";
import ProfilePage from "./pages/ProfilePage";
import FriendRequestPage from "./pages/FriendRequestPage";
import ChatRoom from "./components/ChatRoom";
import ClubPage from "./pages/ClubPage";
import ClubCreate from "./components/ClubCreate";

function App() {

  const router = createBrowserRouter([
    {
      path: '/',
      element: <Root/>,
      errorElement: <ErrorPage/>,
      children: [
        {index: true, element: <CalenderLogPage/>},
        {path: '/exercise', element: <ExercisePage/>},
        {path: '/routine', element: <RoutinePage/>},
        {path: '/post', element: <PostPage/>},
        {path: '/post/upload', element: <PostUpload/>},
        {path: '/login', element: <LoginPage/>},
        {path: '/profile', element: <ProfilePage/>},
        {path: '/friend', element: <FriendRequestPage/>},
        {path: '/chat/:roomId', element: <ChatRoom/>},
        {path: '/club', element: <ClubPage/>},
        {path: '/club/create', element: <ClubCreate/>}
      ]
    }
  ]);

  return (
      <RouterProvider router={router}/>
  );
}

export default App;
