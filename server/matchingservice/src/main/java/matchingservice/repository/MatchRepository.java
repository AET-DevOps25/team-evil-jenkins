package matchingservice.repository;

import matchingservice.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findTop1ByUserIdOrderByCreatedAtDesc(String userId);

    List<Match> findByUserIdOrderByScoreDesc(String userId);
}
