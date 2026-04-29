package chat.controller;

import chat.message.ChatMessage;
import chat.service.ChatMessageService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatHistoryController {

    private final ChatMessageService service;

    public ChatHistoryController(ChatMessageService service) {
        this.service = service;
    }

    /// Приватная история
    @GetMapping("/private/{userA}/{userB}")
    public List<ChatMessage> getPrivateChat(
            @PathVariable String userA,
            @PathVariable String userB) {

        return service.getPrivateChat(userA, userB);
    }

    /// Публичная история
    @GetMapping("/public")
    public List<ChatMessage> getPublicChat() {
        return service.getPublicChat();
    }
}

