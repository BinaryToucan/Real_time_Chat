package chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {

                    @Override
                    protected Principal determineUser(
                            ServerHttpRequest request,
                            WebSocketHandler wsHandler,
                            Map<String, Object> attributes) {

                        String query = request.getURI().getQuery();

                        String username = "anonymous";

                        if (query != null && query.startsWith("username=")) {
                            username = query.substring(9);
                        }

                        String finalUsername = username;

                        return () -> finalUsername;
                    }
                })
                .withSockJS();
    }
}

