#!/bin/bash
# Messaging System Endpoint Testing Script
# Run this after fixing the database schema

BASE_URL="http://localhost:8081"

echo "=========================================="
echo "  Dev-Connect Backend API Testing"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Register User 1 (Developer)
echo -e "${YELLOW}1. Registering User 1 (Alice - Developer)${NC}"
USER1=$(curl -s -X POST "$BASE_URL/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Alice Developer",
    "email": "alice@example.com",
    "password": "password123",
    "role": "DEVELOPER"
  }')
echo "$USER1" | jq . 2>/dev/null || echo "$USER1"
USER1_ID=$(echo "$USER1" | jq -r '.id' 2>/dev/null)
echo -e "${GREEN}User 1 ID: $USER1_ID${NC}"
echo ""

# Test 2: Register User 2 (Client)
echo -e "${YELLOW}2. Registering User 2 (Bob - Client)${NC}"
USER2=$(curl -s -X POST "$BASE_URL/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Bob Client",
    "email": "bob@example.com",
    "password": "password123",
    "role": "CLIENT"
  }')
echo "$USER2" | jq . 2>/dev/null || echo "$USER2"
USER2_ID=$(echo "$USER2" | jq -r '.id' 2>/dev/null)
echo -e "${GREEN}User 2 ID: $USER2_ID${NC}"
echo ""

# Test 3: Get all users
echo -e "${YELLOW}3. Getting all users${NC}"
curl -s "$BASE_URL/api/users/all" | jq . 2>/dev/null || curl -s "$BASE_URL/api/users/all"
echo ""

# Test 4: Send a message from User 1 to User 2
echo -e "${YELLOW}4. Sending message from User 1 to User 2${NC}"
MESSAGE=$(curl -s -X POST "$BASE_URL/api/messages/send" \
  -H "Content-Type: application/json" \
  -d "{
    \"senderId\": $USER1_ID,
    \"receiverId\": $USER2_ID,
    \"text\": \"Hello Bob! This is Alice. How can I help with your project?\"
  }")
echo "$MESSAGE" | jq . 2>/dev/null || echo "$MESSAGE"
echo ""

# Test 5: Send a reply
echo -e "${YELLOW}5. Sending reply from User 2 to User 1${NC}"
REPLY=$(curl -s -X POST "$BASE_URL/api/messages/send" \
  -H "Content-Type: application/json" \
  -d "{
    \"senderId\": $USER2_ID,
    \"receiverId\": $USER1_ID,
    \"text\": \"Hi Alice! I need help building a web application.\"
  }")
echo "$REPLY" | jq . 2>/dev/null || echo "$REPLY"
echo ""

# Test 6: Get conversation between users
echo -e "${YELLOW}6. Getting conversation between User 1 and User 2${NC}"
curl -s "$BASE_URL/api/messages/conversation?userId1=$USER1_ID&userId2=$USER2_ID" | jq . 2>/dev/null || \
curl -s "$BASE_URL/api/messages/conversation?userId1=$USER1_ID&userId2=$USER2_ID"
echo ""

# Test 7: Get User 1's chat list
echo -e "${YELLOW}7. Getting User 1's chat list${NC}"
curl -s "$BASE_URL/api/messages/chats/$USER1_ID" | jq . 2>/dev/null || \
curl -s "$BASE_URL/api/messages/chats/$USER1_ID"
echo ""

# Test 8: Get User 2's chat list
echo -e "${YELLOW}8. Getting User 2's chat list${NC}"
curl -s "$BASE_URL/api/messages/chats/$USER2_ID" | jq . 2>/dev/null || \
curl -s "$BASE_URL/api/messages/chats/$USER2_ID"
echo ""

# Test 9: Update User 1 status to ONLINE
echo -e "${YELLOW}9. Setting User 1 status to ONLINE${NC}"
curl -s -X PUT "$BASE_URL/api/messages/status/$USER1_ID?status=ONLINE" | jq . 2>/dev/null || \
curl -s -X PUT "$BASE_URL/api/messages/status/$USER1_ID?status=ONLINE"
echo ""

# Test 10: Get User 1 status
echo -e "${YELLOW}10. Getting User 1 status${NC}"
curl -s "$BASE_URL/api/messages/status/$USER1_ID" | jq . 2>/dev/null || \
curl -s "$BASE_URL/api/messages/status/$USER1_ID"
echo ""

# Test 11: Search for users
echo -e "${YELLOW}11. Searching for users with 'Alice'${NC}"
curl -s "$BASE_URL/api/users/search?query=Alice" | jq . 2>/dev/null || \
curl -s "$BASE_URL/api/users/search?query=Alice"
echo ""

echo -e "${GREEN}=========================================="
echo "  Testing Complete!"
echo -e "==========================================${NC}"
echo ""
echo "Summary of Created Resources:"
echo "- User 1 (Alice): ID = $USER1_ID"
echo "- User 2 (Bob): ID = $USER2_ID"
echo "- Messages exchanged: 2"
echo ""
echo "Next steps:"
echo "1. Check WebSocket functionality with a WebSocket client"
echo "2. Test typing indicators"
echo "3. Test mark as read functionality"
