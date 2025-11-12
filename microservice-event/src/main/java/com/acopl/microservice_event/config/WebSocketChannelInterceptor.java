package com.acopl.microservice_event.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.acopl.microservice_event.security.JwtUtil;

/**
 * Interceptor for WebSocket STOMP connections to validate JWT tokens.
 * Extracts JWT from the Authorization header and validates it before allowing connection.
 */
@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public WebSocketChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                try {
                    // Validate token and extract claims
                    Long userId = jwtUtil.extractUserId(token);
                    String email = jwtUtil.extractEmail(token);

                    if (userId != null && email != null) {
                        // Create authentication token
                        UsernamePasswordAuthenticationToken auth = 
                            new UsernamePasswordAuthenticationToken(
                                userId.toString(), null, java.util.Collections.emptyList()
                            );
                        
                        // Set authentication in accessor
                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        throw new IllegalArgumentException("Invalid token claims");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("Missing Authorization header");
            }
        }

        return message;
    }
}
