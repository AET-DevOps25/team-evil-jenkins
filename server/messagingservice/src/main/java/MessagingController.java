import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/messaging")
public class MessagingController {
    @Autowired
    private MessagingService messagingService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam String fromUserId,
            @RequestParam String toUserId,
            @RequestParam String message) {
        return ResponseEntity.ok(messagingService.sendMessage(fromUserId, toUserId, message));
    }
}
