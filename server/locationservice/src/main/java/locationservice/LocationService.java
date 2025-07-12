package locationservice;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import model.UserDTO;

@Service
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location updateLocation(String userId, double latitude, double longitude) {
        Location loc = new Location(userId, latitude, longitude, Instant.now());
        repository.save(loc);
        return loc;
    }

    public List<String> searchPartnerByArea(String userId, double radiusKm) {
        Optional<Location> currentUserLocation = repository.findById(userId);
        if (currentUserLocation.isEmpty()) {
            throw new IllegalArgumentException("User does not exist!");
            //return List.of();
        }
        Location loc = currentUserLocation.get();

        return repository.findNearbyUserIds(userId, loc.getLatitude(), loc.getLongitude(), radiusKm);
    }

    public Location getLocation(String userId) {
        return repository.findById(userId).orElse(null);
    }

    public java.util.List<Location> getAll() {
        return repository.findAll();
    }

    public boolean delete(String userId) {
        if( repository.existsById(userId))
        {
            repository.deleteById(userId);
            return true;

        }
        return false;
    }
}

