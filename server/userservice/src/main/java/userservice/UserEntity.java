package userservice;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "location_id")
    private String locationId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_group_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "group_id")
    private List<String> groupIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_sport_interest_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport_id")
    public List<String> sportInterestIds;

    public UserEntity() {
    }

    public UserEntity(String id, String name) {
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

    public String getLocationId() {
        return locationId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public List<String> getSportInterestIds() {
        return sportInterestIds;
    }

    public void setSportInterestIds(List<String> sportInterestIds) {
        this.sportInterestIds = sportInterestIds;
    }

    public void sendMessage(String groupId, String message) {
        // TODO: Implement message sending logic using the groupId
    }

    public void registerUser() {
        // TODO: Implement user registration logic
    }
}