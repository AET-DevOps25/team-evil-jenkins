package userservice;

import org.springframework.stereotype.Service;

import model.LocationDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional; // Import Optional

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LocationServiceClient locationServiceClient;

    public UserService(UserRepository repo, LocationServiceClient client) {
        this.userRepository = repo;
        this.locationServiceClient = client;
    }

    public User getUserById(String id) {
        // findById now returns an Optional. Use orElse(null) or throw an exception.
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return null;
        }
        return userOptional.orElse(null);
    }

    public void addUser(User user) {
        // The save method works for both creating and updating entities
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean updateUser(String id, String firstName, String lastName, String bio, String skillLevel,
            Map<String, List<String>> availability, List<String> sports) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return false;
        if (firstName != null || lastName != null) {
            user.setName(String.format("%s %s", firstName != null ? firstName : user.getName().split(" ")[0],
                    lastName != null ? lastName : (user.getName().contains(" ") ? user.getName().split(" ", 2)[1] : ""))
                    .trim());
        }
        if (bio != null)
            user.setBio(bio);
        if (skillLevel != null)
            user.setSkillLevel(skillLevel);
        if (availability != null)
            user.setAvailability(availability);
        if (sports != null)
            user.setSportInterests(sports);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUserById(String id) {
        // deleteById returns void. First, check if the user exists.
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Return true on successful deletion
        }
        return false; // Return false if the user was not found
    }

    public List<User> findNearbyUsers(String userId, double radius) {
        // Business logic can live here, before or after the call
        List<String> userIds = locationServiceClient.searchPartnerByArea(userId, radius);
        List<User> users = userRepository.findAllById(userIds);
        return users;
    }
}
