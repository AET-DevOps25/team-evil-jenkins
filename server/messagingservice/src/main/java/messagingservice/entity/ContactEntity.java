package messagingservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "contact_id"}))
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "contact_id", nullable = false)
    private String contactId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected ContactEntity() {
    }

    public ContactEntity(String userId, String contactId, LocalDateTime createdAt) {
        this.userId = userId;
        this.contactId = contactId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getContactId() { return contactId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
