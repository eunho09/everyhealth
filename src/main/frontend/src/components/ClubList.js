import React, { useState, useEffect } from 'react';
import { Plus, Search } from 'lucide-react';
import ClubPoster from '../components/ClubPoster';
import '../styles/ClubList.css';
import axios from "axios";
import api from "../services/api";
import {clubService} from "../services/ClubService";

const ClubListPage = () => {
    const [clubs, setClubs] = useState([]);
    const [searchClubName, setSearchClubName] = useState('');
    const [isMyClubs, setIsMyClubs] = useState('all');
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchClubList = async () => {
            try {
                const data = await clubService.findAll();
                setClubs(data);
            } catch (error){
                console.error(error);
            }
        }

        fetchClubList();

        setLoading(false);
    }, []);


    const fetchSearch = async () => {
        try {
            const data = await clubService.findByNameAndMyClub(searchClubName, isMyClubs)
            setClubs(data);
        } catch(error){
            console.log(error);
        }
    }

    const handleCreateClick = () => {
        // 클럽 생성 페이지로 이동
        window.location.href = '/club/create';
    };

    return (
        <div className="club-page">
            <div className="club-page-container">
                <div className="club-page-header">
                    <h1 className="club-page-title">클럽 목록</h1>
                    <button className="create-button" onClick={handleCreateClick}>
                        <Plus size={20} />
                        새 클럽 만들기
                    </button>
                </div>

                <div className="search-section">
                    <input
                        type="text"
                        placeholder="클럽 검색..."
                        className="search-input"
                        value={searchClubName}
                        onChange={(e) => setSearchClubName(e.target.value)}
                    />
                    <select
                        className="filter-select"
                        value={isMyClubs}
                        onChange={(e) => setIsMyClubs(e.target.value)}
                    >
                        <option value="0">전체</option>
                        <option value="1">나의 클럽</option>
                    </select>
                    <button onClick={fetchSearch}>
                        검색
                    </button>
                </div>

                {loading ? (
                    <div>로딩 중...</div>
                ) : (
                    <div className="club-grid">
                        {clubs.map((club) => (
                            <ClubPoster key={club.id} clubData={club} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ClubListPage;