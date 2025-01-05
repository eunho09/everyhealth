import React from 'react';
import '../styles/Comment.css';

const Comment = ({ author, text, createdAt }) => {
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diffTime = Math.abs(now - date);
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
        const diffHours = Math.floor(diffTime / (1000 * 60 * 60));
        const diffMinutes = Math.floor(diffTime / (1000 * 60));

        if (diffDays > 7) {
            return date.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        } else if (diffDays > 0) {
            return `${diffDays}일 전`;
        } else if (diffHours > 0) {
            return `${diffHours}시간 전`;
        } else if (diffMinutes > 0) {
            return `${diffMinutes}분 전`;
        } else {
            return '방금 전';
        }
    };

    return (
        <div className="comment">
            <div className="comment-header">
                <div className="comment-author-info">
                    <span className="comment-author-name">{author}</span>
                </div>
                <span className="comment-date">{formatDate(createdAt)}</span>
            </div>
            <p className="comment-text">{text}</p>
        </div>
    );
};

export default Comment;