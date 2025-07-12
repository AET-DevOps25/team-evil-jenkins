package messagingservice.repository;

import messagingservice.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE (m.fromUserId = :userA AND m.toUserId = :userB) OR (m.fromUserId = :userB AND m.toUserId = :userA) ORDER BY m.timestamp ASC")
    Page<MessageEntity> findConversation(@Param("userA") String userA,
                                         @Param("userB") String userB,
                                         Pageable pageable);
}
