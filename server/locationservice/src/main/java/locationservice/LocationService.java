package locationservice;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import model.Location;
import model.User;

@Service
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location updateLocation(String userId, double latitude, double longitude) {
        Location loc = new Location(userId, latitude, longitude, Instant.now());
        repository.saveLocation(loc);
        return loc;
    }

    public List<User> searchPartnerByArea(String userId, double radiusKm) {
        return repository.findUsersWithinRadius(userId, radiusKm);
    }

    public Location getLocation(String userId) {
        return repository.findByUserId(userId);
    }

    public List<Location> getAll() {
        return repository.findAll();
    }

    public boolean delete(String userId) {
        return repository.deleteByUserId(userId);
    }
}
