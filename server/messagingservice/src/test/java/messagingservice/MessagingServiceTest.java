package messagingservice;

import messagingservice.entity.MessageEntity;
import messagingservice.entity.ContactEntity;
import messagingservice.repository.MessageRepository;
import messagingservice.repository.ContactRepository;
import messagingservice.client.UserServiceClient;
import messagingservice.dto.MessageDto;
import messagingservice.dto.ContactDto;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit tests for MessagingService
 */
@ExtendWith(MockitoExtension.class)
class MessagingServiceTest {
    
    @Mock
    private MessageRepository messageRepository;
    
    @Mock
    private ContactRepository contactRepository;
    
    @Mock
    private UserServiceClient userServiceClient;
    
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    
    @InjectMocks
    private MessagingService messagingService;
    
    @Test
    void sendMessage_ShouldSaveMessageAndSendWebSocketNotification() {
        // Given
        MessageEntity savedMessage = new MessageEntity("user1", "user2", "Hello World", LocalDateTime.now());
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(savedMessage);
        
        // When
        MessageDto result = messagingService.sendMessage("user1", "user2", "Hello World");
        
        // Then
        assertNotNull(result);
        assertEquals("user1", result.fromUserId());
        assertEquals("user2", result.toUserId());
        assertEquals("Hello World", result.content());
        
        verify(messageRepository).save(any(MessageEntity.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/conversation.user1-user2"), any(MessageDto.class));
    }
    
    @Test
    void getConversation_ShouldReturnPaginatedMessages() {
        // Given
        List<MessageEntity> messages = Arrays.asList(
            new MessageEntity("user1", "user2", "Hi", LocalDateTime.now().minusMinutes(2)),
            new MessageEntity("user2", "user1", "Hello", LocalDateTime.now().minusMinutes(1))
        );
        Page<MessageEntity> messagePage = new PageImpl<>(messages, PageRequest.of(0, 10), 2);
        
        when(messageRepository.findConversation(eq("user1"), eq("user2"), any(Pageable.class)))
            .thenReturn(messagePage);
        
        // When
        Page<MessageDto> result = messagingService.getConversation("user1", "user2", 0, 10);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals("Hi", result.getContent().get(0).content());
        assertEquals("Hello", result.getContent().get(1).content());
    }
    
    @Test
    void addContact_ShouldSaveContactAndReturnDto() {
        // Given
        when(contactRepository.existsByUserIdAndContactId("user1", "user2")).thenReturn(false);
        
        // When
        ContactDto result = messagingService.addContact("user1", "user2");
        
        // Then
        assertNotNull(result);
        assertEquals("user1", result.userId());
        assertEquals("user2", result.contactId());
        
        verify(contactRepository, times(2)).save(any(ContactEntity.class));
    }
    
    @Test
    void getContacts_ShouldReturnUserContacts() {
        // Given
        List<String> contactIds = Arrays.asList("user2", "user3");
        List<UserDTO> expectedUsers = Arrays.asList(
            new UserDTO("user2", "User Two", "", "", "", java.util.Map.of(), java.util.List.of()),
            new UserDTO("user3", "User Three", "", "", "", java.util.Map.of(), java.util.List.of())
        );
        
        when(contactRepository.findContactIdsByUser("user1")).thenReturn(contactIds);
        when(userServiceClient.getUsers(contactIds)).thenReturn(expectedUsers);
        
        // When
        List<UserDTO> result = messagingService.getContacts("user1");
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user2", result.get(0).id());
        assertEquals("user3", result.get(1).id());
    }
}
