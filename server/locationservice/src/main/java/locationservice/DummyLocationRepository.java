package locationservice;

import model.Location;
import model.User;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class DummyLocationRepository implements LocationRepository {
    private final Map<String, Location> locationMap = new ConcurrentHashMap<>();
    private final List<User> dummyUsers = new ArrayList<>();

    public DummyLocationRepository() {
        // Initialize with some dummy users for testing
        dummyUsers.add(new User("user1", "Alice"));
        dummyUsers.add(new User("user2", "Bob"));
        dummyUsers.add(new User("user3", "Charlie"));
        dummyUsers.add(new User("user4", "Diana"));
        dummyUsers.add(new User("user5", "Evan"));
        
        // Add some initial locations
        saveLocation(new Location("user1", 48.137154, 11.576124, Instant.now())); // Munich
        saveLocation(new Location("user2", 48.138234, 11.579876, Instant.now())); // Near Munich
        saveLocation(new Location("user3", 48.135678, 11.582345, Instant.now())); // Near Munich
        saveLocation(new Location("user4", 48.856614, 2.352222, Instant.now()));  // Paris
        saveLocation(new Location("user5", 40.712776, -74.005974, Instant.now())); // New York
    }

    @Override
    public Location findByUserId(String userId) {
        return locationMap.get(userId);
    }

    @Override
    public void saveLocation(Location location) {
        locationMap.put(location.getUserId(), location);
    }

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locationMap.values());
    }

    @Override
    public boolean deleteByUserId(String userId) {
        return locationMap.remove(userId) != null;
    }

    @Override
    public List<User> findUsersWithinRadius(String userId, double radius) {
        Location current = findByUserId(userId);
        if (current == null) return new ArrayList<>();
        
        double lat1 = current.getLatitude();
        double lon1 = current.getLongitude();
        
        return dummyUsers.stream()
            .filter(u -> {
                Location loc = locationMap.get(u.id());
                if (loc == null || u.id().equals(userId)) return false;
                double dist = haversine(lat1, lon1, loc.getLatitude(), loc.getLongitude());
                return dist <= radius;
            })
            .collect(Collectors.toList());
    }
    
    // Haversine formula for distance in kilometers
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
