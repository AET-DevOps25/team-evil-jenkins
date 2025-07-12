package locationservice;

import model.DTOEntityMapper;
import model.LocationDTO;

public class LocationMapper implements DTOEntityMapper<Location, LocationDTO>{

    @Override
    public LocationDTO toDTO(Location entity) {
        return new LocationDTO(entity.getUserId(), entity.getLatitude(), entity.getLongitude(), entity.getTimestamp());
    }

    @Override
    public Location toEntity(LocationDTO dto) {
        return new Location(dto.userId(), dto.latitude(), dto.longitude(), dto.timestamp());
    }
}
