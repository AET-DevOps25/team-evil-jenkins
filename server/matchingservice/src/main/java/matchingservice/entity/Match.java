package matchingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity representing a single match result between two users.
 */
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "matched_user_id", nullable = false)
    private String matchedUserId;

    @Column(name = "score")
    private Double score;

    @Column(name = "reason", length = 1024)
    private String reason;

    @Column(name = "common_preferences", length = 2048)
    private String commonPreferences;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Match() {
        // JPA
    }

    public Match(String userId, String matchedUserId, Double score, String reason, String commonPreferences) {
        this.userId = userId;
        this.matchedUserId = matchedUserId;
        this.score = score;
        this.reason = reason;
        this.commonPreferences = commonPreferences;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMatchedUserId() {
        return matchedUserId;
    }

    public Double getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    public String getCommonPreferences() {
        return commonPreferences;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
