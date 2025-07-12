package messagingservice.dto;

import java.time.LocalDateTime;

public record MessageDto(Long id, String fromUserId, String toUserId, String content, LocalDateTime timestamp) {
}
