import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import {useEffect, useState, useCallback, useRef} from 'react';
import {useLocation, useParams} from "react-router-dom";
import "../styles/ChatRoom.css";

const ChatRoom = () => {
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [isConnected, setIsConnected] = useState(false);  // 연결 상태 추가
    const {roomId} = useParams();
    const messagesEndRef = useRef(null);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/chat'),
            onConnect: () => {
                console.log('Connected!');
                setIsConnected(true);  // 연결 상태 업데이트

                client.subscribe(`/topic/public/rooms/${roomId}`, (response) => {
                    const receivedMessage = JSON.parse(response.body);
                    console.log(receivedMessage.body);
                    const cleanMessage = {
                        ...receivedMessage.body,
                        message: receivedMessage.body.message.replace(/^"|"$/g, '')
                    };
                    setMessages(prev => [...prev, cleanMessage]);
                });
            },
            onDisconnect: () => {
                console.log('Disconnected!');
                setIsConnected(false);  // 연결 해제 상태 업데이트
            },
            // 재연결 설정 추가
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        })
;
        client.activate();
        setStompClient(client);

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




    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const currentUserId = "1";

    return (
        <div className="chat-container">
            {/* 채팅방 헤더 */}
            <div className="chat-header">
                <h1>채팅방 #{roomId}</h1>
                {!isConnected && (
                    <div className="connecting-status">
                        <div className="loading-spinner"></div>
                        <span>연결 중...</span>
                    </div>
                )}
            </div>

            {/* 메시지 영역 */}
            <div className="messages-container">
                {messages.map((msg, index) => {
                    const isMyMessage = msg.member.id === currentUserId;

                    return (
                        <div key={index} className={`message-wrapper ${isMyMessage ? 'my-message' : 'other-message'}`}>
                            {!isMyMessage && (
                                <div className="profile-image">
                                    {msg.member.profileImage ? (
                                        <img
                                            src={msg.member.profileImage}
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
                                    {msg.member.profileImage ? (
                                        <img
                                            src={msg.member.profileImage}
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

            {/* 입력 영역 */}
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