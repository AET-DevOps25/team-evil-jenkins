package locationservice;

import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public LocationEntity updateLocation(String id, String name, double latitude, double longitude) {
        LocationEntity entity = new LocationEntity(id, name, latitude, longitude);
        return repository.save(entity);
    }

    public java.util.Optional<LocationEntity> getLocation(String id) {
        return repository.findById(id);
    }

    public java.util.List<LocationEntity> getAll() {
        return repository.findAll();
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public java.util.List<LocationEntity> searchWithinRadius(double latitude, double longitude, double radiusKm) {
        return repository.findWithinRadius(latitude, longitude, radiusKm);
    }
}

