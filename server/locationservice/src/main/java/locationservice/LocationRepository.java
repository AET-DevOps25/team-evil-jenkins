package locationservice;

import model.Location;
import model.User;

import java.util.List;

public interface LocationRepository {
    Location findByUserId(String userId);
    void saveLocation(Location location);
    List<Location> findAll();
    boolean deleteByUserId(String userId);
    List<User> findUsersWithinRadius(String userId, double radius);
}
