package messagingservice;

import messagingservice.dto.MessageDto;
import messagingservice.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessagingServiceTest {

    @Autowired
    private MessageRepository repo;

    private MessagingService service;
    private SimpMessagingTemplate template;

    @BeforeEach
    void setup() {
        template = org.mockito.Mockito.mock(SimpMessagingTemplate.class);
        service = new MessagingService(repo, template);
    }

    @Test
    void sendAndRetrieveConversation() {
        service.sendMessage("alice", "bob", "hi");
        service.sendMessage("bob", "alice", "hello");

        List<MessageDto> msgs = service.getConversation("alice", "bob", 0, 10).getContent();
        assertThat(msgs).hasSize(2);
        assertThat(msgs.get(0).content()).isEqualTo("hi");
        assertThat(msgs.get(1).content()).isEqualTo("hello");
    }

    }
