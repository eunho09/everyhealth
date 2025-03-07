import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaPlus } from "react-icons/fa";
import { FaRegMessage } from "react-icons/fa6";
import { Link } from "react-router-dom";
import '../styles/PostList.css'
import Comment from "./Comment";
import {postService} from "../services/postService";
import {commentService} from "../services/commentService";

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [images, setImages] = useState({});
    const [showCommentModal, setShowCommentModal] = useState(false);
    const [activePostId, setActivePostId] = useState(null);
    const [comment, setComment] = useState("");
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [limit] = useState(5);

    // 초기 게시물 로드
    useEffect(() => {
        const fetchInitialPosts = async () => {
            setLoading(true);
            try {
                const data = await postService.findAll(limit);

                setPosts(data);
                setHasMore(data.length >= limit);
            } catch (error) {
                console.error("초기 포스트 데이터 가져오기 실패:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchInitialPosts();
    }, [limit]);

    // 스크롤 이벤트 리스너 설정
    useEffect(() => {
        const handleScroll = () => {
            // 현재 스크롤 위치 + 화면 높이가 문서 전체 높이에 가까워지면 추가 데이터 로드
            if (
                window.innerHeight + window.scrollY >= document.body.offsetHeight - 200 &&
                !loading &&
                hasMore &&
                posts.length > 0
            ) {
                loadMorePosts();
            }
        };

        window.addEventListener('scroll', handleScroll);

        // 컴포넌트 언마운트 시 이벤트 리스너 제거
        return () => window.removeEventListener('scroll', handleScroll);
    }, [loading, hasMore, posts.length]);

    // 추가 게시물 로드 (스크롤)
    const loadMorePosts = async () => {
        if (loading || !hasMore || posts.length === 0) return;

        const lastPostId = posts[posts.length - 1].id;

        setLoading(true);
        try {
            const data = await postService.scroll(limit, lastPostId);

            if (data.length === 0) {
                setHasMore(false);
            } else {
                setPosts(prevPosts => [...prevPosts, ...data]);
                setHasMore(data.length >= limit);
            }
        } catch (error) {
            console.error("스크롤 포스트 데이터 가져오기 실패:", error);
        } finally {
            setLoading(false);
        }
    };

    // 이미지 로드
    useEffect(() => {
        const newPosts = posts.filter(post => !images[post.id]);
        if (newPosts.length === 0) return;

        const fetchImages = async () => {
            try {
                const imageRequests = newPosts.map((post) =>
                    postService.getPostImageUrl(post.imageUrl));
                const imageResponses = await Promise.all(imageRequests);

                const newImageMap = {};
                imageResponses.forEach((res, idx) => {
                    const post = newPosts[idx];
                    const imageUrl = URL.createObjectURL(res.data);
                    newImageMap[post.id] = {
                        url: imageUrl,
                        width: 0,
                        height: 0
                    };
                });

                const loadImageSizes = Object.keys(newImageMap).map(async (postId) => {
                    return new Promise((resolve) => {
                        const img = new Image();
                        img.onload = () => {
                            newImageMap[postId].width = img.width;
                            newImageMap[postId].height = img.height;
                            resolve();
                        };
                        img.src = newImageMap[postId].url;
                    });
                });

                await Promise.all(loadImageSizes);
                setImages(prevImages => ({ ...prevImages, ...newImageMap }));
            } catch (error) {
                console.error("이미지 로드 실패:", error);
            }
        };

        fetchImages();
    }, [posts]);

    const calculateAspectRatio = (width, height) => {
        return (height / width * 100).toFixed(2);
    };

    const handleCommentClick = async (postId) => {
        setActivePostId(postId);
        setShowCommentModal(true);
        try {
            const data = await commentService.findByPostId(postId);
            setComments(data);
        } catch (error) {
            console.error("댓글 불러오기 실패:", error);
            setComments([]);
        }
    };

    const handleCommentSubmit = async () => {
        if (!comment.trim()) {
            alert("댓글을 입력해주세요.");
            return;
        }

        try {
            await commentService.save(activePostId, comment);

            const data = await commentService.findByPostId(activePostId);
            setComments(data);
            setComment("");
        } catch (error) {
            console.error("댓글 추가 실패:", error);
            alert("댓글 추가 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="post-container">
            <div className="post-feed">
                <Link to="/post/upload" className="upload-button">
                    <FaPlus/>
                </Link>

                {posts.map((post) => {
                    const imageData = images[post.id] || {};
                    const aspectRatio = imageData.width && imageData.height
                        ? calculateAspectRatio(imageData.width, imageData.height)
                        : 100;

                    return (
                        <article
                            key={post.id}
                            className="post-card"
                        >
                            <div
                                className="image-container"
                                style={{paddingBottom: `${aspectRatio}%`}}
                            >
                                <img
                                    src={`/api/images/${post.imageUrl}`}
                                    alt="포스트 이미지"
                                    className="post-image"
                                />
                            </div>
                            <div className="text-container">
                                <div className="action-buttons">
                                    <button
                                        className="action-button"
                                        onClick={() => handleCommentClick(post.id)}
                                    >
                                        <FaRegMessage/>
                                    </button>
                                </div>
                                <p className="post-texts">{post.text}</p>
                            </div>
                        </article>
                    );
                })}

                {loading && <div className="loading">로딩 중...</div>}
                {!hasMore && posts.length > 0 && <div className="no-more-posts">더 이상 게시물이 없습니다.</div>}

                {showCommentModal && (
                    <div className="modal-overlay" onClick={() => setShowCommentModal(false)}>
                        <div className="modal-content" onClick={e => e.stopPropagation()}>
                            <h3 className="modal-title">댓글</h3>
                            <div className="comments-list">
                                {comments.length > 0 ? (
                                    comments.map((comment) => (
                                        <Comment
                                            key={comment.id}
                                            author={comment.name}
                                            text={comment.text}
                                            createdAt={comment.localDateTime}
                                        />
                                    ))
                                ) : (
                                    <p className="no-comments">작성된 댓글이 없습니다.</p>
                                )}
                            </div>
                            <div className="comment-input-container">
                                <textarea
                                    className="comment-input"
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                    placeholder="댓글을 입력하세요..."
                                    maxLength={1000}  // 최대 글자수 제한
                                />
                                <div className="button-container">
                                    <button
                                        className="modal-button submit-button"
                                        onClick={handleCommentSubmit}
                                        disabled={!comment.trim()}  // 내용이 없으면 버튼 비활성화
                                    >
                                        작성
                                    </button>
                                    <button
                                        className="modal-button cancel-button"
                                        onClick={() => {
                                            setShowCommentModal(false);
                                            setComments([]);
                                        }}
                                    >
                                        닫기
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default PostList;