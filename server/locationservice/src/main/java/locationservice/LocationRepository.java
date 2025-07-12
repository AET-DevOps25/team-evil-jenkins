package locationservice;

import model.UserDTO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LocationRepository extends JpaRepository<Location, String> {
    
        @Query(value = "SELECT new model.UserDTO(l.userId, u.name) FROM Location l JOIN User u ON l.userId = u.id WHERE l.userId != :userId AND " +
            "( 6371 * acos( cos( radians(:lat) ) * cos( radians( l.latitude ) ) * " +
            "cos( radians( l.longitude ) - radians(:lon) ) + sin( radians(:lat) ) * " +
            "sin( radians( l.latitude ) ) ) ) <= :radius")
    List<UserDTO> findUsersWithinRadius(
            @Param("userId") String userId,
            @Param("lat") double latitude,
            @Param("lon") double longitude,
            @Param("radius") double radiusKm
    );

}
