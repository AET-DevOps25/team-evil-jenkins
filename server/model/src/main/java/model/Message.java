package model;

import java.time.LocalDateTime;

public class Message {
    private String id;
    private String content;
    private LocalDateTime timestamp;
    private Group group;

    public Message() {}

    public Message(String id, String content, LocalDateTime timestamp, Group group) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.group = group;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public void deleteMessage() {
        // TODO: Implement deletion logic
    }
    public void editMessage(String newContent) {
        // TODO: Implement edit logic
        this.content = newContent;
    }
}
