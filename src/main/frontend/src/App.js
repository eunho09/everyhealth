import React from "react";
import './App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Root from "./pages/Root";
import ErrorPage from "./pages/ErrorPage";
import MyCalendar from "./components/MyCalendar";
import Exercise from "./pages/Exercise";

function App() {

  const router = createBrowserRouter([
    {
      path: '/',
      element:<Root/>,
      errorElement: <ErrorPage/>,
      children: [
        {index: true, element: <MyCalendar/>},
        {path: '/exercise', element: <Exercise/>}
      ]
    }
  ])

  return (
      <RouterProvider router={router}/>
  );
}

export default App;
