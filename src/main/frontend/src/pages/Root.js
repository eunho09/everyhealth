import React from "react";
import Sidebar from "../components/Sidebar";
import {Outlet} from "react-router-dom";
import Layout from "../components/Layout";

const Root = () => {
    return(
        <>
            <div className="container">
                <Sidebar/>
                <div className="content">
                    <Outlet/>
                </div>
            </div>
        </>
    )
}

export default Root;
