package userservice;

import java.util.List;
import java.util.Map;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true)
    private String email;

    private String bio;

    private String skillLevel;

    @Transient
    private Map<String, List<String>> availability;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_group_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "group_id")
    private List<String> groupIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_sport_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport_interests")
    public List<String> sportInterests;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Map<String, List<String>> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, List<String>> availability) {
        this.availability = availability;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public List<String> getSportInterests() {
        return sportInterests;
    }

    public void setSportInterests(List<String> sportInterests) {
        this.sportInterests = sportInterests;
    }

    public void sendMessage(String groupId, String message) {
        // TODO: Implement message sending logic using the groupId
    }

    public void registerUser() {
        // TODO: Implement user registration logic
    }
}