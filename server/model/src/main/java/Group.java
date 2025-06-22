package model;

import java.util.List;
import java.time.LocalDateTime;

public class Group {
<<<<<<< HEAD
    private String id;
    private String name;
    private User admin;
    private List<User> participants;
    private List<Message> messages;
    private LocalDateTime eventDate;
    private Sport sport;

    public Group() {
    }

    public Group(String id, String name, User admin, List<User> participants, List<Message> messages,
            LocalDateTime eventDate, Sport sport) {
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

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void addParticipant(User participant) {
        participants.add(participant);
    }

    public void removeParticipant(User participant) {
        participants.remove(participant);
    }

    public void sendMessage(String message) {
        // TODO: Implement message sending logic
        messages.add(newMessage);
    }

    public void createMeetingSuggestion(LocalDateTime date) {
        // TODO: Implement meeting suggestion creation logic
        setEventDate(date);
    }
}
=======
    
}
>>>>>>> origin/main
