package locationservice;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LocationRepository extends JpaRepository<Location, String> {
    
        @Query(value = "SELECT l.id FROM locations l WHERE l.id != :userId AND " +
                        "( 6371 * acos( cos( radians(:lat) ) * cos( radians( l.latitude ) ) * " +
                        "cos( radians( l.longitude ) - radians(:lon) ) + sin( radians(:lat) ) * " +
                        "sin( radians( l.latitude ) ) ) ) <= :radius", nativeQuery = true)
        List<String> findNearbyUserIds(
                        @Param("userId") String userId,
                        @Param("lat") double latitude,
                        @Param("lon") double longitude,
                        @Param("radius") double radiusKm);
}
