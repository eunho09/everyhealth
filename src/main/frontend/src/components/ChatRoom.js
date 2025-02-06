import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useEffect, useState, useCallback } from 'react';

const ChatRoom = ({ roomId }) => {
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [isConnected, setIsConnected] = useState(false);  // 연결 상태 추가

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/chat'),
            onConnect: () => {
                console.log('Connected!');
                setIsConnected(true);  // 연결 상태 업데이트

                client.subscribe(`/topic/public/rooms/${roomId}`, (response) => {
                    const receivedMessage = JSON.parse(response.body);
                    setMessages(prev => [...prev, receivedMessage]);
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
        });

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
                body: JSON.stringify({
                    message: messageInput
                })
            });
            setMessageInput('');
        } catch (error) {
            console.error('메시지 전송 실패:', error);
        }
    }, [messageInput, stompClient, isConnected, roomId]);

    return (
        <div>
            {!isConnected && (
                <div style={{color: 'red'}}>연결 중...</div>
            )}
            <div className="chat-messages">
                {messages.map((msg, index) => (
                    <div key={index}>
                        <span>{msg.sender}: </span>
                        <span>{msg.message}</span>
                    </div>
                ))}
            </div>
            <div className="chat-input">
                <input
                    type="text"
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                    disabled={!isConnected}  // 연결되지 않았을 때 비활성화
                />
                <button
                    onClick={sendMessage}
                    disabled={!isConnected}  // 연결되지 않았을 때 비활성화
                >
                    Send
                </button>
            </div>
        </div>
    );
};

export default ChatRoom;