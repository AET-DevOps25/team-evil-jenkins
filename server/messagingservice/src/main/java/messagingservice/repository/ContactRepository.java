package messagingservice.repository;

import messagingservice.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    @Query("SELECT c.contactId FROM ContactEntity c WHERE c.userId = :uid")
    List<String> findContactIdsByUser(@Param("uid") String userId);

    boolean existsByUserIdAndContactId(String userId, String contactId);
}
