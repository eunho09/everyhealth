import React, {useState} from "react";
import { FiPlusCircle } from "react-icons/fi";
import "../styles/Plus.css";



const Plus = () => {
    return (
        <button className="plus-button">
            <FiPlusCircle />
        </button>
    )
}
export default Plus;