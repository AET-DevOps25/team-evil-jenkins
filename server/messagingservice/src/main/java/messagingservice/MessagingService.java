package messagingservice;

import messagingservice.entity.MessageEntity;
import messagingservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.List;
import messagingservice.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
public class MessagingService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessagingService(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Persist a new message and return the saved entity.
     */
    public MessageDto sendMessage(String fromUserId, String toUserId, String content) {
        MessageEntity entity = new MessageEntity(fromUserId, toUserId, content, LocalDateTime.now());
        MessageEntity saved = messageRepository.save(entity);
        MessageDto dto = toDto(saved);
        String convId = buildConversationId(fromUserId, toUserId);
        messagingTemplate.convertAndSend("/topic/conversation." + convId, dto);
        return dto;
    }

    /**
     * Retrieve the full conversation between the two users sorted by timestamp.
     */
    public Page<MessageDto> getConversation(String userA, String userB, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findConversation(userA, userB, pageable).map(this::toDto);
    }

    private String buildConversationId(String a, String b) {
        return (a.compareTo(b) < 0 ? a + "-" + b : b + "-" + a);
    }

    private MessageDto toDto(MessageEntity e) {
        return new MessageDto(e.getId(), e.getFromUserId(), e.getToUserId(), e.getContent(), e.getTimestamp());
    }
}
