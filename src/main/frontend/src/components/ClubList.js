import React, { useState, useEffect } from 'react';
import { Plus, Search } from 'lucide-react';
import ClubPoster from '../components/ClubPoster';
import '../styles/ClubList.css';
import axios from "axios";
import api from "../api/api";

const ClubListPage = () => {
    const [clubs, setClubs] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [filter, setFilter] = useState('all');
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchClubList = async () => {
            try {
                const response = await api.get("api/clubs")
                setClubs(response.data);
            } catch (error){
                console.error(error);
            }
        }

        fetchClubList();

        setLoading(false);
    }, []);

    useEffect(() => {
        console.log(`${clubs}`);
    }, [clubs]);

    const filteredClubs = clubs.filter(club => {
        const matchesSearch = club.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            club.content.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesFilter = filter === 'all' || club.level.toLowerCase() === filter.toLowerCase();
        return matchesSearch && matchesFilter;
    });

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
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <select
                        className="filter-select"
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    >
                        <option value="all">전체</option>
                        <option value="아마추어">아마추어</option>
                        <option value="세미프로">세미프로</option>
                    </select>
                </div>

                {loading ? (
                    <div>로딩 중...</div>
                ) : (
                    <div className="club-grid">
                        {filteredClubs.map((club) => (
                            <ClubPoster key={club.id} clubData={club} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ClubListPage;