package userservice;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // Import Optional

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserById(String id) {
        // findById now returns an Optional. Use orElse(null) or throw an exception.
        Optional<UserEntity> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public void addUser(UserEntity user) {
        // The save method works for both creating and updating entities
        userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserById(String id) {
        // deleteById returns void. First, check if the user exists.
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Return true on successful deletion
        }
        return false; // Return false if the user was not found
    }
}
