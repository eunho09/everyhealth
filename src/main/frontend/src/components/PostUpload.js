import React, { useState } from "react";
import axios from "axios";

const PostUpload = () => {
    const [file, setFile] = useState(null);
    const [preview, setPreview] = useState(null);
    const [text, setText] = useState("");

    // 파일 선택 시 Base64로 변환하여 미리보기 설정
    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);

        // Base64로 미리보기 생성
        const reader = new FileReader();
        reader.onloadend = () => {
            setPreview(reader.result); // Base64로 변환된 이미지 데이터 저장
        };
        reader.readAsDataURL(selectedFile);
    };

    // 텍스트 입력 처리
    const handleTextChange = (e) => {
        setText(e.target.value);
    };

    // 서버로 파일 및 텍스트 전송
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!file || !text) {
            alert("파일과 내용을 모두 입력해주세요!");
            return;
        }

        const formData = new FormData();
        formData.append("file", file); // 파일 추가
        formData.append("text", text); // 텍스트 추가

        try {
            const response = await axios.post("/api/post", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            console.log("응답:", response.data);
            alert("업로드 성공!");
        } catch (error) {
            console.error("업로드 실패:", error);
            alert("업로드 실패!");
        }
    };

    return (
        <div style={styles.container}>
            <h2>포스트 업로드</h2>
            <form onSubmit={handleSubmit} style={styles.form}>
                <input
                    type="file"
                    accept="image/*"
                    onChange={handleFileChange}
                    style={styles.fileInput}
                />
                {preview && <img src={preview} alt="미리보기" style={styles.imagePreview} />}

                <textarea
                    value={text}
                    onChange={handleTextChange}
                    style={styles.textArea}
                />

                <button type="submit" style={styles.submitButton}>
                    업로드
                </button>
            </form>
        </div>
    );
};

const styles = {
    container: {
        maxWidth: "500px",
        margin: "20px auto",
        padding: "20px",
        border: "1px solid #ccc",
        borderRadius: "10px",
        textAlign: "center",
        backgroundColor: "#f9f9f9",
    },
    form: {
        display: "flex",
        flexDirection: "column",
        gap: "10px",
    },
    fileInput: {
        padding: "10px",
    },
    imagePreview: {
        width: "100%",
        maxHeight: "300px",
        objectFit: "cover",
        marginTop: "10px",
        borderRadius: "10px",
    },
    textArea: {
        width: "100%",
        minHeight: "100px",
        padding: "10px",
        fontSize: "16px",
        borderRadius: "5px",
        border: "1px solid #ccc",
    },
    submitButton: {
        padding: "10px 20px",
        fontSize: "16px",
        borderRadius: "5px",
        border: "none",
        backgroundColor: "#007bff",
        color: "#fff",
        cursor: "pointer",
    },
};

export default PostUpload;
