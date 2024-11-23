import React from "react";
import './App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Root from "./pages/Root";
import ErrorPage from "./pages/ErrorPage";
import WorkoutCalendar from "./components/WorkoutCalendar";
import Exercise from "./pages/Exercise";
import Routine from "./pages/Routine";
import CalenderLog from "./pages/CalenderLog";

function App() {

  const router = createBrowserRouter([
    {
      path: '/',
      element:<Root/>,
      errorElement: <ErrorPage/>,
      children: [
        {index: true, element: <CalenderLog/>},
        {path: '/exercise', element: <Exercise/>},
        {path: '/routine', element: <Routine/>}
      ]
    }
  ])

  return (
      <RouterProvider router={router}/>
  );
}

export default App;
