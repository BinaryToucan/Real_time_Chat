package chat.controller;

import chat.user.User;
import chat.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public UserController(UserService userService,
                          SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/user.list")
    @SendTo("/topic/users")
    public List<String> getUsers() {
        return userService.getUsersNames();
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            userService.removeUser(username);
            messagingTemplate.convertAndSend("/topic/users", userService.getUsersNames());
        }
    }

    @MessageMapping("/user.add")
    public void addUser(User user, SimpMessageHeaderAccessor headerAccessor) {
        //System.out.println("Add user");
        headerAccessor.getSessionAttributes().put("username",
                user.getUsername());

        userService.save(user);
        messagingTemplate.convertAndSend("/topic/users", userService.getUsersNames());
    }
}