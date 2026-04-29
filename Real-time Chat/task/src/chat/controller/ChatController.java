package chat.controller;

import chat.enums.MessageType;
import chat.message.ChatMessage;
import chat.service.ChatMessageService;
import chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private final List<ChatMessage> history = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private ChatMessageService service;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {

        message.setTimestamp(LocalDateTime.now());

        service.save(message);

        if (message.getType() == MessageType.PRIVATE) {
            messagingTemplate.convertAndSendToUser(
                    message.getReceiver(),
                    "/queue/messages",
                    message
            );

        } else {
            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }


    @MessageMapping("/chat.history")
    @SendTo("/topic/history")
    public List<ChatMessage> getHistory() {
        return history;
    }
}
