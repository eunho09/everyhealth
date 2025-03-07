import React from 'react';
import { useParams } from 'react-router-dom';
import '../styles/Profile.css';
import FriendProfile from "../components/FriendProfile";

const FriendProfilePage = () => {
    const { friendId } = useParams();

    return (
        <FriendProfile
            friendId={friendId}
        />
    );
};

export default FriendProfilePage;