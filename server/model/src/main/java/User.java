package model;

import java.util.List;

public class User {
    private String name;
    private String id;
    private Location location;
    private List<Group> groups;
    public List<Sport> sportInterests;

    public User() {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Sport> getSportInterests() {
        return sportInterests;
    }

    public void setSportInterests(List<Sport> sportInterests) {
        this.sportInterests = sportInterests;
    }
     public User findPartner(IPartnerMatcher partnerMatcher) {
        return partnerMatcher.findPartner(this, null);
    }
    public void sendMessage(Group group, String message) {
        //TODO: Implement message sending logic
    }
    public void registerUser() {
        //TODO: Implement user registration logic
    }
}