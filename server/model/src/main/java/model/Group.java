package model;

import java.util.List;
import java.time.LocalDateTime;

public class Group {
    private String id;
    private String name;
    private UserDTO admin;
    private List<UserDTO> participants;
    private List<Message> messages;
    private LocalDateTime eventDate;
    private SportDTO sport;

    public Group() {
    }

    public Group(String id, String name, UserDTO admin, List<UserDTO> participants, List<Message> messages,
            LocalDateTime eventDate, SportDTO sport) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.participants = participants;
        this.messages = messages;
        this.eventDate = eventDate;
        this.sport = sport;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserDTO admin) {
        this.admin = admin;
    }

    public List<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDTO> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }

    public void addParticipant(UserDTO participant) {
        participants.add(participant);
    }

    public void removeParticipant(UserDTO participant) {
        participants.remove(participant);
    }

    public void sendMessage(String message) {
        Message newMessage = new Message();
        newMessage.setContent(message);
        // TODO: Implement message sending logic
        messages.add(newMessage);
    }

    public void createMeetingSuggestion(LocalDateTime date) {
        // TODO: Implement meeting suggestion creation logic
        setEventDate(date);
    }
}
