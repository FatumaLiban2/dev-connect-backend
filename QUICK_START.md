# Messaging System - Quick Start Guide

## 🎉 What's Been Implemented

Your backend messaging system is now complete! Here's what you have:

### ✅ Core Features
1. **Real-time WebSocket messaging** - Users can chat in real-time
2. **Message persistence** - All messages saved to PostgreSQL
3. **Read receipts** - Sent, Delivered, Read statuses
4. **Typing indicators** - See when someone is typing
5. **Online/Offline status** - Track user availability
6. **Unread message counts** - Know how many unread messages you have
7. **Role-based chats** - Clients see Developers, Developers see Clients

### 📁 Project Structure
```
src/main/java/org/devconnect/devconnectbackend/
├── model/
│   ├── User.java          - User entity with roles (CLIENT/DEVELOPER)
│   ├── Message.java       - Message entity with status tracking
│   └── Chat.java          - Chat thread between two users
├── dto/
│   ├── MessageDTO.java    - Message data transfer object
│   ├── ChatDTO.java       - Chat list data transfer object
│   ├── TypingIndicatorDTO.java
│   └── UserStatusDTO.java
├── repository/
│   ├── UserRepository.java
│   ├── MessageRepository.java
│   └── ChatRepository.java
├── service/
│   ├── MessageService.java - Message business logic
│   ├── ChatService.java    - Chat management logic
│   └── UserService.java    - User status management
├── controller/
│   ├── WebSocketController.java - WebSocket message handlers
│   └── MessageController.java   - REST API endpoints
├── config/
│   ├── WebSocketConfig.java - WebSocket STOMP configuration
│   └── CorsConfig.java      - CORS settings for frontend
└── listener/
    └── WebSocketEventListener.java - Connection/disconnection handling
```

## 🚀 How to Run

### 1. Set up PostgreSQL Database
```bash
# Create a database
createdb devconnect

# Set environment variable
export DATABASE_URL=jdbc:postgresql://localhost:5432/devconnect?user=youruser&password=yourpassword
```

### 2. Run the Application
```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

### 3. WebSocket Endpoint
```
ws://localhost:8080/ws
```

## 🔌 API Endpoints

### REST APIs
```
GET    /api/messages/chats/{userId}                    - Get all chats for user
GET    /api/messages/conversation?userId1=1&userId2=2  - Get messages between users
POST   /api/messages/send                              - Send a message
PUT    /api/messages/read?senderId=1&receiverId=2      - Mark messages as read
PUT    /api/messages/status/{userId}?status=ONLINE     - Update user status
GET    /api/messages/status/{userId}                   - Get user status
```

### WebSocket Destinations
```javascript
// Subscribe to:
/user/queue/messages        - Receive messages
/user/queue/typing          - Receive typing indicators
/user/queue/read-receipts   - Receive read receipts
/topic/user-status          - Receive status updates

// Send to:
/app/chat                   - Send message
/app/typing                 - Send typing indicator
/app/messages-read          - Mark messages as read
```

## 🧪 Testing

### Quick Test with cURL
```bash
# Send a message
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "senderId": 1,
    "receiverId": 2,
    "text": "Hello!",
    "projectId": 10
  }'
```

### Run Tests
```bash
./gradlew test
```
Tests use H2 in-memory database automatically.

## 🔗 Frontend Integration

Your frontend already has the UI components. You just need to:

1. **Install WebSocket dependencies:**
```bash
npm install @stomp/stompjs sockjs-client
```

2. **Connect to WebSocket:**
```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const client = new Client({
  webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
  onConnect: () => {
    // Subscribe to messages
    client.subscribe('/user/queue/messages', (message) => {
      const data = JSON.parse(message.body);
      console.log('New message:', data);
    });
  }
});

client.activate();
```

3. **Send messages:**
```javascript
client.publish({
  destination: '/app/chat',
  body: JSON.stringify({
    senderId: 1,
    receiverId: 2,
    text: "Hello!",
    projectId: 10
  })
});
```

See `MESSAGING_README.md` for complete integration guide!

## ⚠️ Important Notes

1. **Authentication Integration**: When your teammate finishes the auth system, you'll need to:
   - Add JWT validation to WebSocket connections
   - Secure REST endpoints with authentication
   - Pass user ID from authenticated session

2. **CORS Configuration**: Update `CorsConfig.java` with your actual frontend URL in production

3. **Database**: Make sure PostgreSQL is running and DATABASE_URL is set

## 📚 Full Documentation

See `MESSAGING_README.md` for:
- Complete API documentation
- Database schema details
- Frontend integration examples
- Security considerations
- Troubleshooting guide

## 🎯 Next Steps

1. Set up your PostgreSQL database
2. Create some test users in the database
3. Test the REST APIs with Postman/cURL
4. Integrate with your React frontend
5. Wait for auth system and then integrate authentication

## 💡 Tips

- The system automatically creates chat threads when first message is sent
- Messages are grouped by date in the frontend
- Unread counts update automatically
- User status broadcasts to all connected clients
- All data persists in PostgreSQL

Happy coding! 🚀
