import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaPlus } from "react-icons/fa";
import { FaRegMessage } from "react-icons/fa6";
import { Link } from "react-router-dom";
import '../styles/PostList.css'
import Comment from "./Comment";

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [images, setImages] = useState({});
    const [showCommentModal, setShowCommentModal] = useState(false);
    const [activePostId, setActivePostId] = useState(null);
    const [comment, setComment] = useState("");
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axios.get("/api/posts");
                setPosts(response.data);
            } catch (error) {
                console.error("포스트 데이터 가져오기 실패:", error);
            }
        };

        fetchPosts();
    }, []);

    useEffect(() => {
        if (posts.length === 0) return;

        const fetchImages = async () => {
            try {
                const imageRequests = posts.map((post) =>
                    axios.get(`/api/images/${post.imageUrl}`, { responseType: "blob" })
                );
                const imageResponses = await Promise.all(imageRequests);

                const imageMap = imageResponses.reduce((acc, res, idx) => {
                    const imageUrl = URL.createObjectURL(res.data);
                    acc[posts[idx].id] = {
                        url: imageUrl,
                        width: 0,
                        height: 0
                    };
                    return acc;
                }, {});

                const loadImageSizes = Object.keys(imageMap).map(async (postId) => {
                    return new Promise((resolve) => {
                        const img = new Image();
                        img.onload = () => {
                            imageMap[postId].width = img.width;
                            imageMap[postId].height = img.height;
                            resolve();
                        };
                        img.src = imageMap[postId].url;
                    });
                });

                await Promise.all(loadImageSizes);
                setImages(imageMap);
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
            const response = await axios.get(`/api/comment/${postId}`);
            setComments(response.data);
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
            await axios.post(`/api/comment`, {
                postId: activePostId,
                text: comment
            });

            const response = await axios.get(`/api/comment/${activePostId}`);
            setComments(response.data);
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
                        <article key={post.id} className="post-card">
                            <div
                                className="image-container"
                                style={{paddingBottom: `${aspectRatio}%`}}
                            >
                                <img
                                    src={imageData.url}
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
                                <p className="post-text">{post.text}</p>
                            </div>
                        </article>
                    );
                })}

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
                                            }
                                        }
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