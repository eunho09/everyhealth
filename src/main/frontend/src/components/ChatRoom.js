import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import React, {useEffect, useState, useCallback, useRef} from 'react';
import {useLocation, useNavigate, useParams} from "react-router-dom";
import "../styles/ChatRoom.css";
import axios from "axios";

const ChatRoom = () => {
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [club, setClub] = useState(null);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [isConnected, setIsConnected] = useState(false);
    const [oldestMessageId, setOldestMessageId] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const {roomId} = useParams();
    const messagesEndRef = useRef(null);
    const messagesContainerRef = useRef(null);

    // 최초 메시지 로딩
    useEffect(() => {
        loadRecentMessages();
    }, [roomId]);

    // 컴포넌트 마운트 시 스크롤 이벤트 리스너 등록
    useEffect(() => {
        const container = messagesContainerRef.current;
        if (container) {
            container.addEventListener('scroll', handleScroll);
            return () => container.removeEventListener('scroll', handleScroll);
        }
    }, [isLoading, hasMore, oldestMessageId]); // 의존성 배열 추가

    // 스크롤 이벤트 처리
    const handleScroll = async (e) => {
        const container = e.target;
        console.log('Scroll event triggered', container.scrollTop); // 스크롤 디버깅

        // 스크롤이 상단에 가까워졌을 때 이전 메시지 로드
        if (container.scrollTop <= 100 && !isLoading && hasMore && oldestMessageId) {
            console.log('Loading older messages...'); // 로딩 디버깅
            await loadOlderMessages();
        }
    };

    const fetchClub = async () => {
        try {
            const response = await axios.get(`/api/club/chatRoom/${roomId}`);
            setClub(response.data);
        } catch (error){
            console.log(error);
        }
    }

    // 최초 메시지 로딩
    const loadRecentMessages = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(`/api/rooms/${roomId}/recentMessage?limit=20`);
            const data = await response.json();
            console.log(data);
            setMessages(data);
            if (data.length > 0) {
                setOldestMessageId(data[0].messageId); // 가장 오래된 메시지의 ID 저장
            }
        } catch (error) {
            console.error('Failed to load recent messages:', error);
        } finally {
            setIsLoading(false);
        }
    };

    // 이전 메시지 로딩
    const loadOlderMessages = async () => {
        if (!oldestMessageId || isLoading) return;

        setIsLoading(true);
        try {
            console.log('Fetching older messages:', oldestMessageId); // 디버깅 로그
            const response = await fetch(`/api/rooms/${roomId}/olderMessage?messageId=${oldestMessageId}`);
            const olderMessages = await response.json();
            console.log('Received older messages:', olderMessages); // 디버깅 로그

            if (olderMessages.length > 0) {
                const container = messagesContainerRef.current;
                const prevHeight = container.scrollHeight;
                const prevScrollTop = container.scrollTop;

                setMessages(prev => [...olderMessages, ...prev]);
                setOldestMessageId(olderMessages[0].messageId);

                // requestAnimationFrame을 사용하여 스크롤 위치 복원
                requestAnimationFrame(() => {
                    const newHeight = container.scrollHeight;
                    container.scrollTop = prevScrollTop + (newHeight - prevHeight);
                });
            } else {
                setHasMore(false);
            }
        } catch (error) {
            console.error('Failed to load older messages:', error);
        } finally {
            setIsLoading(false);
        }
    };

    // WebSocket 연결 및 메시지 구독
    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/chat'),
            onConnect: () => {
                console.log('Connected!');
                setIsConnected(true);

                client.subscribe(`/topic/public/rooms/${roomId}`, (response) => {
                    const receivedMessage = JSON.parse(response.body);
                    setMessages(prev => [...prev, receivedMessage.body]);
                    scrollToBottom();
                });
            },
            onDisconnect: () => {
                console.log('Disconnected!');
                setIsConnected(false);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        client.activate();
        setStompClient(client);
        fetchClub();

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [roomId]);

    const sendMessage = useCallback(() => {
        if (!messageInput.trim()) return;

        // 연결 상태 확인
        if (!stompClient || !isConnected) {
            console.log('STOMP 연결이 없습니다.');
            return;
        }

        try {
            stompClient.publish({
                destination: `/app/chat/rooms/${roomId}/send`,
                body: JSON.stringify(messageInput)
            });
            setMessageInput('');
        } catch (error) {
            console.error('메시지 전송 실패:', error);
        }
    }, [messageInput, stompClient, isConnected, roomId]);

    const scrollToBottom = useCallback(() => {
        const container = messagesContainerRef.current;
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }, []);

    useEffect(() => {
        scrollToBottom();
    }, [messages, scrollToBottom]);

    const handleLeaveRoom = async (chatRoomId) => {
        // 확인 메시지 표시
        const isConfirmed = window.confirm("채팅방을 나가시겠습니까? 방장인 경우 클럽이 삭제됩니다.");

        if (isConfirmed) {
            try {
                const response = await axios.delete(`/api/club/${club.id}/leave`);

                if (response.status === 200) {
                    alert("채팅방에서 나갔습니다.");
                    navigate("/club");
                }
            } catch (error) {
                console.error("채팅방 나가기 실패:", error);
                alert("채팅방 나가기에 실패했습니다.");
            }
        }
    };

    const currentUserId = "1";

    return (
        <div className="chat-container">
            <div className="chat-header">
                <h1>채팅방 #{roomId}</h1>
                <button onClick={handleLeaveRoom}>나가기</button>
                {!isConnected && (
                    <div className="connecting-status">
                        <div className="loading-spinner"></div>
                        <span>연결 중...</span>
                    </div>
                )}
            </div>

            <div
                className="messages-container"
                ref={messagesContainerRef}
                style={{
                    height: 'calc(100vh - 150px)',
                    overflowY: 'auto',
                    padding: '20px',
                    display: 'flex',
                    flexDirection: 'column'
                }}
            >
                {isLoading && (
                    <div className="loading-indicator">메시지를 불러오는 중...</div>
                )}
                {!hasMore && (
                    <div className="no-more-messages">이전 메시지가 없습니다</div>
                )}
                {messages.map((msg, index) => {
                    const isMyMessage = msg.member.id === currentUserId;

                    return (
                        <div key={index} className={`message-wrapper ${isMyMessage ? 'my-message' : 'other-message'}`}>
                            {!isMyMessage && (
                                <div className="profile-image">
                                    {msg.member.picture ? (
                                        <img
                                            src={msg.member.picture}
                                            alt={`${msg.member.name}'s profile`}
                                        />
                                    ) : (
                                        <div className="profile-initial">
                                            {msg.member.name.charAt(0)}
                                        </div>
                                    )}
                                </div>
                            )}
                            <div className="message-content-wrapper">
                                {!isMyMessage && (
                                    <div className="message-sender">{msg.member.name}</div>
                                )}
                                <div className="message-bubble">
                                    <div className="message-content">{msg.message}</div>
                                </div>
                            </div>
                            {isMyMessage && (
                                <div className="profile-image">
                                    {msg.member.picture ? (
                                        <img
                                            src={msg.member.picture}
                                            alt="My profile"
                                        />
                                    ) : (
                                        <div className="profile-initial">
                                            {msg.member.name.charAt(0)}
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>
                    );
                })}
                <div ref={messagesEndRef}/>
            </div>

            <div className="input-container">
                <input
                    type="text"
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                    disabled={!isConnected}
                    placeholder="메시지를 입력하세요..."
                    className="message-input"
                />
                <button
                    onClick={sendMessage}
                    disabled={!isConnected}
                    className="send-button"
                >
                    전송
                </button>
            </div>
        </div>
    );
};

export default ChatRoom;