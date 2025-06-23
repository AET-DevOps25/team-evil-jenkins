package userservice;

import model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DummyUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User findById(String id) {
        Optional<User> user = users.stream()
                                   .filter(u -> u.getId().equals(id))
                                   .findFirst();
        return user.orElse(null);
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean deleteById(String id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}