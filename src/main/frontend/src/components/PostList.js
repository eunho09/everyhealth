import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaPlus } from "react-icons/fa";
import { Link } from "react-router-dom";
import { FaRegMessage } from "react-icons/fa6";


const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [images, setImages] = useState({});
    const [showCommentModal, setShowCommentModal] = useState(false); // 댓글 모달 상태
    const [activePostId, setActivePostId] = useState(null); // 댓글 작성 중인 포스트 ID
    const [comment, setComment] = useState(""); // 입력된 댓글
    const [comments, setComments] = useState([]); // 현재 포스트의 댓글 리스트

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

    const handleCommentSubmit = async () => {
        if (!comment.trim()) {
            alert("댓글을 입력해주세요.");
            return;
        }

        try {
            await axios.post(`/api/comment`,
                {
                    postId: activePostId,
                    text: comment
                });
            alert("댓글이 성공적으로 추가되었습니다!");
            setComment(""); // 댓글 초기화

            // 새로 작성된 댓글 리스트 다시 불러오기
            const response = await axios.get(`/api/comment/${activePostId}`);
            console.log(response.data);
            setComments(response.data);
        } catch (error) {
            console.error("댓글 추가 실패:", error);
            alert("댓글 추가 중 오류가 발생했습니다.");
        }
    };



    return (
        <div style={styles.container}>
            <div style={styles.feed}>
                <button style={styles.uploadButton}>
                    <Link to="/post/upload">
                        <FaPlus style={styles.plusIcon} />
                    </Link>
                </button>
                {posts.map((post) => {
                    const imageData = images[post.id] || {};
                    const aspectRatio = imageData.width && imageData.height
                        ? calculateAspectRatio(imageData.width, imageData.height)
                        : 177.78;

                    return (
                        <div key={post.id} style={styles.postCard}>
                            <div
                                style={{
                                    ...styles.imageContainer,
                                    paddingTop: `${aspectRatio}%`
                                }}
                            >
                                <img
                                    src={imageData.url}
                                    alt="포스트 이미지"
                                    style={{
                                        ...styles.image,
                                        objectPosition: 'center'
                                    }}
                                />
                            </div>
                            <div style={styles.textContainer}>
                                <button onClick={() => {
                                    setShowCommentModal(true);
                                    setActivePostId(post.id);
                                }}>
                                    <FaRegMessage />
                                </button>
                                <p style={styles.text}>{post.text}</p>
                            </div>
                        </div>
                    );
                })}
                {showCommentModal && (
                    <div style={styles.modal}>
                        <div style={styles.modalContent}>
                            <h3>댓글</h3>
                            <div style={styles.commentList}>
                                {comments.length > 0 ? (
                                    comments.map((c) => (
                                        <div key={c.id} style={styles.comment}>
                                            <p style={styles.commentAuthor}>{c.author}</p>
                                            <p style={styles.commentText}>{c.text}</p>
                                        </div>
                                    ))
                                ) : (
                                    <p style={styles.noComments}>작성된 댓글이 없습니다.</p>
                                )}
                            </div>
                            <textarea
                                style={styles.textarea}
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="댓글을 입력하세요..."
                            />
                            <div style={styles.buttonContainer}>
                                <button style={styles.submitButton} onClick={handleCommentSubmit}>
                                    작성
                                </button>
                                <button
                                    style={styles.cancelButton}
                                    onClick={() => setShowCommentModal(false)}
                                >
                                    닫기
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

const styles = {
    container: {
        width: '100%',
        display: 'flex',
        justifyContent: 'center',
        backgroundColor: '#fafafa', // 인스타그램 배경색과 유사
    },
    feed: {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "20px", // 포스트 간 간격 증가
        padding: "20px 10px",
        width: '100%',
        maxWidth: "450px", // 데스크톱에서 더 넓은 최대 너비
    },
    uploadButton: {
        backgroundColor: "#007bff",
        color: "#fff",
        padding: "15px 20px",
        borderRadius: "50%",
        position: "fixed",
        bottom: "30px",
        right: "30px",
        zIndex: "1000",
        boxShadow: "0px 6px 10px rgba(0, 0, 0, 0.3)",
    },
    plusIcon: {
        fontSize: "24px",
    },
    postCard: {
        width: "100%",
        border: "1px solid #e0e0e0",
        borderRadius: "15px",
        overflow: "hidden",
        backgroundColor: "#fff",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.08)",
        transition: "transform 0.2s",
        transformOrigin: "center",
    },
    imageContainer: {
        width: "100%",
        height: "0",
        position: "relative",
        backgroundColor: "#f0f0f0",
    },
    image: {
        position: "absolute",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        objectFit: "cover",
    },
    textContainer: {
        backgroundColor: "#fff",
        padding: "12px 15px",
        borderTop: "1px solid #e0e0e0",
    },
    text: {
        color: "#000",
        fontSize: "16px",
        lineHeight: "1.5",
        textAlign: "center",
    },
    commentIcon: {
        fontSize: "18px",
        color: "#007bff",
        cursor: "pointer",
    },
    modal: {
        position: "fixed",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        backgroundColor: "rgba(0, 0, 0, 0.5)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: "1000",
    },
    modalContent: {
        width: "80%",
        maxWidth: "400px",
        backgroundColor: "#fff",
        padding: "20px",
        borderRadius: "10px",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
    },
    textarea: {
        width: "100%",
        height: "100px",
        padding: "10px",
        borderRadius: "5px",
        border: "1px solid #ccc",
        resize: "none",
    },
    submitButton: {
        backgroundColor: "#007bff",
        color: "#fff",
        padding: "10px 15px",
        border: "none",
        borderRadius: "5px",
        cursor: "pointer",
        marginTop: "10px",
    },
    cancelButton: {
        backgroundColor: "#f44336",
        color: "#fff",
        padding: "10px 15px",
        border: "none",
        borderRadius: "5px",
        cursor: "pointer",
        marginTop: "10px",
        marginLeft: "10px",
    },
    commentList: {
        maxHeight: "200px",
        overflowY: "auto",
        marginBottom: "10px",
        border: "1px solid #ccc",
        borderRadius: "5px",
        padding: "10px",
        backgroundColor: "#f9f9f9",
    },
    comment: {
        marginBottom: "10px",
        paddingBottom: "5px",
        borderBottom: "1px solid #eaeaea",
    },
    commentAuthor: {
        fontWeight: "bold",
        marginBottom: "5px",
    },
    commentText: {
        margin: "0",
    },
    noComments: {
        textAlign: "center",
        color: "#888",
    },
    buttonContainer: {
        display: "flex",
        justifyContent: "space-between",
    },
};

export default PostList;