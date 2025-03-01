// PostUpload.jsx
import React, { useState, useRef } from 'react';
import axios from 'axios';
import { Upload, Send } from 'lucide-react';
import '../styles/PostUpload.css';

const PostUpload = () => {
    const [file, setFile] = useState(null);
    const [preview, setPreview] = useState(null);
    const [text, setText] = useState('');
    const [isDragging, setIsDragging] = useState(false);
    const fileInputRef = useRef(null);

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        handleFile(selectedFile);
    };

    const handleFile = (selectedFile) => {
        if (selectedFile) {
            setFile(selectedFile);
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreview(reader.result);
            };
            reader.readAsDataURL(selectedFile);
        }
    };

    const handleDragOver = (e) => {
        e.preventDefault();
        setIsDragging(true);
    };

    const handleDragLeave = (e) => {
        e.preventDefault();
        setIsDragging(false);
    };

    const handleDrop = (e) => {
        e.preventDefault();
        setIsDragging(false);
        const droppedFile = e.dataTransfer.files[0];
        handleFile(droppedFile);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!file || !text) {
            alert('파일과 내용을 모두 입력해주세요!');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('text', text);

        try {
            const response = await axios.post('/api/post', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log('응답:', response.data);
            alert('업로드 성공!');
            // Reset form
            setFile(null);
            setPreview(null);
            setText('');

            window.location.href = "/posts";
        } catch (error) {
            console.error('업로드 실패:', error);
            alert('업로드 실패!');
        }
    };

    return (
        <div className="upload-container">
            <h2 className="upload-title">포스트 업로드</h2>
            <form onSubmit={handleSubmit} className="upload-form">
                <div
                    className={`upload-area ${isDragging ? 'dragging' : ''}`}
                    onDragOver={handleDragOver}
                    onDragLeave={handleDragLeave}
                    onDrop={handleDrop}
                    onClick={(e) => {
                        // 현재 target이 upload-area인 경우에만 click 이벤트 실행
                        if (e.currentTarget === e.target) {
                            fileInputRef.current?.click();
                        }
                    }}
                >
                    <Upload className="upload-icon"/>
                    <p className="upload-text">
                        이미지를 드래그하거나 클릭하여 업로드하세요
                    </p>
                    <p className="upload-subtext">PNG, JPG, GIF (최대 10MB)</p>
                    <input
                        ref={fileInputRef}
                        type="file"
                        onChange={handleFileChange}
                        accept="image/*"
                        className="file-input"
                    />
                </div>

                {preview && (
                    <div className="preview-container">
                        <img src={preview} alt="미리보기" className="preview-image"/>
                        <div className="preview-overlay">
                        </div>
                    </div>
                )}

                <textarea
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                    placeholder="게시글 내용을 입력하세요..."
                    className="text-area"
                />

                <button type="submit" className="submit-button">
                    <Send />
                    업로드하기
                </button>
            </form>
        </div>
    );
};

export default PostUpload;