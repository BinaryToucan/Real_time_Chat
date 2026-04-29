package chat.service;

import chat.enums.MessageType;
import chat.message.ChatMessage;
import chat.message.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository repository;

    public ChatMessageService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    /// Сохранение сообщений
    public void save(ChatMessage message) {
        repository.save(message);
    }

    ///  Получение сообщений между 2 юзерами
    public List<ChatMessage> getPrivateChat(String userA, String userB) {
        return repository.findPrivateMessages(userA, userB);
    }

    /// Публичный чат
    public List<ChatMessage> getPublicChat() {
        return repository.findByTypeOrderByTimestampAsc(MessageType.PUBLIC);
    }
}

