package locationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {
    @Query("SELECT l FROM LocationEntity l WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(l.latitude)))) <= :radius")
    List<LocationEntity> findWithinRadius(@Param("lat") double latitude, @Param("lon") double longitude, @Param("radius") double radiusKm);
}
