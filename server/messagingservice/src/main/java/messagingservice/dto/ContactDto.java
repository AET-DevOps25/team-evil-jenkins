package messagingservice.dto;

import java.time.LocalDateTime;

public record ContactDto(String userId, String contactId, LocalDateTime createdAt) {
}
