package chat.message;

import chat.enums.MessageType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/// Сообщения в чате
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Кто отправляет
    @NonNull
    private String sender;

    ///  Куда отправляет
    private final String receiver = null;

    @Column(columnDefinition = "TEXT")
    @NonNull
    private String content;

    @Enumerated(EnumType.STRING)
    @NonNull
    private MessageType type;

    private LocalDateTime timestamp;
}
