package userservice;

import model.User;

import java.util.List;

public interface UserRepository {
    User findById(String id);
    void save(User user);
    List<User> findAll();
    boolean deleteById(String id);
}