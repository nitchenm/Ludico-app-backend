# microservice-event

Microservice for managing game events (Ludico)

Endpoints:
- POST /api/events -> create event
- GET  /api/events -> list events
- POST /api/events/{id}/join?userId= -> join event
- POST /api/events/{id}/leave?userId= -> leave event
- POST /api/events/{id}/images -> upload image (multipart file)

WebSocket (STOMP):
- Endpoint: /ws-events (SockJS fallback)
- Send to application prefix: /app/chat
- Subscribe to topic: /topic/chat

Files stored locally under `uploads/events/{eventId}`
