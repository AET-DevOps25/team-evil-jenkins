package messagingservice.dto;

import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {
    @NotBlank
    private String fromUserId;
    @NotBlank
    private String toUserId;
    @NotBlank
    private String content;

    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }
    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
