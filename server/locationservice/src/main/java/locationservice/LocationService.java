package locationservice;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import model.Location;
import model.User;

@Service
public class LocationService {
    // In-memory storage for user locations
    private final Map<String, Location> locationMap = new ConcurrentHashMap<>();

    // TODO: Replace with real user repository/service
    private final List<User> dummyUsers = new ArrayList<>();

    public Location updateLocation(String userId, double latitude, double longitude) {
        Location location = new Location(userId, latitude, longitude, Instant.now());
        locationMap.put(userId, location);
        return location;
    }

    public List<User> searchPartnerByArea(String userId, double radius) {
        Location current = locationMap.get(userId);
        if (current == null) return Collections.emptyList();
        double lat1 = current.getLatitude();
        double lon1 = current.getLongitude();
        // For demo, search all dummy users (replace with actual user DB/service)
        return dummyUsers.stream()
            .filter(u -> {
                Location loc = locationMap.get(u.getId());
                if (loc == null || u.getId().equals(userId)) return false;
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
