# 🚀 LUDICO BACKEND - QUICK START GUIDE

## Pre-requisites
- Java 17+
- Maven 3.8.9+
- Ports available: 8050, 8080, 8086, 8761, 8888, 8099

---

## 📦 Installation & Setup

### 1. Build the Project
```bash
cd "c:\Users\nitch\Desktop\Proyectos Freelance\Ludico-backend\Ludico-app-backend"
mvn clean install -DskipTests
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: 22 seconds
```

### 2. Start Services (in this order)

Open 6 terminal windows and run one command per window:

**Terminal 1 - Config Server (8888)**
```bash
cd microservice-config
mvn spring-boot:run
# Wait for: "Started MicroserviceConfigApplication"
```

**Terminal 2 - Eureka Registry (8761)**
```bash
cd microservice-eureka
mvn spring-boot:run
# Check: http://localhost:8761/
```

**Terminal 3 - API Gateway (8086)**
```bash
cd microservice-gateway
mvn spring-boot:run
```

**Terminal 4 - User Service (8050)**
```bash
cd microservice-user
mvn spring-boot:run
# JWT authentication service
```

**Terminal 5 - Event Service (8080)**
```bash
cd microservice-event
mvn spring-boot:run
# Event CRUD + WebSocket chat
```

**Terminal 6 - Swagger Central (8099)**
```bash
cd microservice-swagger-central
mvn spring-boot:run
# API documentation
```

All services should show: `Started MicroserviceXyzApplication in X seconds`

---

## 🔐 API Usage Examples

### 1️⃣ Register a New User

```bash
curl -X POST "http://localhost:8050/auth/register" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=john@example.com&password=MyPassword123&name=John Doe"
```

**Response:**
```json
{
  "userId": 1,
  "email": "john@example.com"
}
```

### 2️⃣ Login & Get JWT Token

```bash
curl -X POST "http://localhost:8050/auth/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=john@example.com&password=MyPassword123"
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzMxMzYwMDAwLCJleHAiOjE3MzEzNjM2MDB9.xyz...",
  "userId": 1,
  "email": "john@example.com"
}
```

**Save the token for next requests!** (Store in browser localStorage or environment variable)

### 3️⃣ Create an Event

```bash
TOKEN="eyJhbGci..." # Use token from login above

curl -X POST "http://localhost:8080/api/events" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Magic The Gathering Night",
    "description": "Casual Magic tournament",
    "gameType": "MTG",
    "latitude": -32.8833,
    "longitude": -68.8458,
    "capacity": 8
  }'
```

**Response:**
```json
{
  "id": 1,
  "title": "Magic The Gathering Night",
  "description": "Casual Magic tournament",
  "gameType": "MTG",
  "creatorId": 1,
  "latitude": -32.8833,
  "longitude": -68.8458,
  "capacity": 8,
  "currentParticipants": 1,
  "createdAt": "2024-11-11T19:00:00"
}
```

### 4️⃣ Get All Events

```bash
curl -X GET "http://localhost:8080/api/events" \
  -H "Authorization: Bearer $TOKEN"
```

### 5️⃣ Join an Event

```bash
curl -X POST "http://localhost:8080/api/events/1/join" \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
{
  "eventId": 1,
  "userId": 1,
  "joinedAt": "2024-11-11T19:00:00"
}
```

### 6️⃣ WebSocket Chat (Real-time Messaging)

**Option A: Using JavaScript (Browser)**

```html
<!DOCTYPE html>
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <div id="messages"></div>
    <input type="text" id="messageInput" placeholder="Type message...">
    <button onclick="sendMessage()">Send</button>

    <script>
    const TOKEN = "eyJhbGci..."; // Your JWT token
    const client = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/ws-events',
        connectHeaders: {
            'Authorization': 'Bearer ' + TOKEN
        },
        onConnect: () => {
            console.log('Connected to WebSocket');
            
            // Subscribe to messages
            client.subscribe('/topic/chat', (message) => {
                const msg = JSON.parse(message.body);
                document.getElementById('messages').innerHTML += 
                    `<p><strong>User ${msg.userId}:</strong> ${msg.content}</p>`;
            });
        },
        onError: (error) => {
            console.error('Connection error:', error);
        }
    });
    
    client.activate();
    
    function sendMessage() {
        const content = document.getElementById('messageInput').value;
        client.publish({
            destination: '/app/chat',
            body: JSON.stringify({
                eventId: 1,
                content: content,
                imagePath: null
            })
        });
        document.getElementById('messageInput').value = '';
    }
    </script>
</body>
</html>
```

**Option B: Using cURL (for testing)**

```bash
# Note: cURL doesn't directly support WebSocket, use wscat instead:
npm install -g wscat

wscat -c "ws://localhost:8080/ws-events" \
  --header "Authorization: Bearer eyJhbGci..."

# Then type:
{"eventId":1,"content":"Hello from chat!","imagePath":null}
```

### 7️⃣ Upload Event Image

```bash
TOKEN="eyJhbGci..." 

curl -X POST "http://localhost:8080/api/events/1/images" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/image.jpg"
```

### 8️⃣ Leave an Event

```bash
curl -X POST "http://localhost:8080/api/events/1/leave" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🌐 Web Interfaces

### Swagger API Documentation
- **Central Hub:** http://localhost:8099/swagger-ui.html
- **User Service:** http://localhost:8050/doc/swagger-ui/index.html
- **Event Service:** http://localhost:8080/doc/swagger-ui/index.html

### Eureka Service Registry
- **Dashboard:** http://localhost:8761/
- Shows all registered microservices and their health

### Config Server
- **Health Check:** http://localhost:8888/health
- **Configuration:** http://localhost:8888/msvc-user/default

---

## 🔑 Token Management

### How JWT Tokens Work

1. **Token contains:**
   - User ID (subject)
   - Email (custom claim)
   - Issue time (iat)
   - Expiration time (exp)
   - Digital signature

2. **Token expiration:**
   - Default: 1 hour (3600000 ms)
   - After expiration: Must login again

3. **Using the token:**
   - Include in every request: `Authorization: Bearer <token>`
   - WebSocket: Send in connection headers

### Token Validation

All requests are validated server-side:
- ✅ Signature is valid
- ✅ Token hasn't expired
- ✅ User ID extracted correctly

If validation fails:
- REST API returns 401 Unauthorized
- WebSocket connection is rejected

---

## 🐛 Troubleshooting

### Service won't start

**Problem:** Port already in use
```bash
# Find process using port 8080 (Windows)
netstat -ano | findstr :8080
# Kill process by PID
taskkill /PID <PID> /F
```

**Problem:** Config Server not responding
```
Make sure microservice-config starts FIRST
Check: http://localhost:8888/health
```

### Authentication fails

**Problem:** 401 Unauthorized
```bash
# Check token is valid
- Token includes "Bearer " prefix
- Token hasn't expired (1 hour max)
- User exists in database
```

**Problem:** WebSocket won't connect
```bash
# Check:
- Token is valid
- Using ws:// not http://
- Header format: Authorization: Bearer <token>
```

### Database errors

**Problem:** Table doesn't exist
```
Event service uses H2 (in-memory)
Tables auto-created on startup
User service needs MySQL (check connection in config)
```

---

## 📊 Database Content

### View Users
```bash
# Connect to MySQL (if using User service DB)
mysql -u root -p ecomarketdtbs

SELECT id, name, email FROM users;
```

### View Events
```bash
# Events use H2 (in-memory) - only in current session
# After restart, data is lost (good for testing!)
```

---

## ✅ Testing Checklist

- [ ] All 6 services started
- [ ] Eureka shows 5 services registered
- [ ] Can register user with /auth/register
- [ ] Can login and receive JWT token
- [ ] Can create event with JWT
- [ ] Can join event with JWT
- [ ] WebSocket chat connects with token
- [ ] Chat messages appear in real-time
- [ ] Non-participants cannot send chat
- [ ] Expired token returns 401

---

## 📝 Notes

### Security
- Never share your JWT token
- JWT tokens are valid for 1 hour
- Password is hashed with BCrypt (secure)
- Each token includes user ID (cannot be spoofed)

### Data
- User data: Persisted in MySQL database
- Event data: Persisted in MySQL/H2 (H2 is in-memory)
- Chat messages: Stored in database
- Images: Saved to `uploads/events/{eventId}/` directory

### Performance
- Config Server caches configurations
- Eureka caches service registrations
- JWT validation is fast (signature check only)
- WebSocket is real-time (no polling)

---

## 🆘 Getting Help

### Check Logs
Each service logs to console:
- **Authentication errors:** Check microservice-user logs
- **WebSocket errors:** Check microservice-event logs
- **Config errors:** Check microservice-config logs

### Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "Missing Authorization header" | No token provided | Add `-H "Authorization: Bearer <token>"` |
| "Invalid JWT signature" | Token invalid/tampered | Get new token from /auth/login |
| "Token expired" | Token > 1 hour old | Login again to get new token |
| "User not authenticated" | Invalid credentials | Check email and password match |
| "User is not a participant" | Can't chat if not joined | Join event first with /events/{id}/join |

---

## 🎓 Learning Resources

- **Spring Security:** https://spring.io/projects/spring-security
- **JWT:** https://jwt.io
- **Spring WebSocket:** https://spring.io/guides/gs/messaging-stomp-websocket/
- **Microservices:** https://microservices.io/

---

## 📞 Support Contact

For issues or questions about the backend:

1. Check **IMPLEMENTATION_CHECKLIST.md** for what's implemented
2. Review **JWT_AUTHENTICATION_GUIDE.md** for security details
3. Read **README.md** for architecture overview
4. Check service logs for error messages

---

**Status: ✅ READY FOR USE**

Happy gaming! 🎲🃏
