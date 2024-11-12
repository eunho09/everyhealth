import React, {useState} from "react";
import { FiPlusCircle } from "react-icons/fi";
import styled from "styled-components";

const Button = styled.button`
  font-size: 50px;
  background: none;
  border: none;
  cursor: pointer;
  
  &:hover{
    color: gray;
  }
`;

const Plus = () => {

    return (
        <Button>
            <FiPlusCircle />
        </Button>
    )
}
export default Plus;