package userservice;

import java.util.List;

import java.util.Map;

public record UpdateUserRequest(String firstName, String lastName, String bio, String skillLevel, Map<String, List<String>> availability, List<String> sports, String picture) {
}
