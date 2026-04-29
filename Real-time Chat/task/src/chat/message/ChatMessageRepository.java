package chat.message;

import chat.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /// Вcе приватные сообщения между userA и userB
    @Query("""
        SELECT m FROM ChatMessage m
        WHERE\s
            (m.sender = :userA AND m.receiver = :userB)
            OR
            (m.sender = :userB AND m.receiver = :userA)
        ORDER BY m.timestamp ASC
   \s""")
    List<ChatMessage> findPrivateMessages(
            @Param("userA") String userA,
            @Param("userB") String userB);


    /// Все сообщения по типу
    List<ChatMessage> findByTypeOrderByTimestampAsc(MessageType type);


    ///  Последние 50 сообщений
    List<ChatMessage> findTop50ByTypeOrderByTimestampDesc(MessageType type);
}

