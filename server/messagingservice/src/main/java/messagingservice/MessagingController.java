package messagingservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import messagingservice.dto.SendMessageRequest;

@RestController
@RequestMapping("/messaging")
public class MessagingController {
    @Autowired
    private MessagingService messagingService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody SendMessageRequest req) {
        return ResponseEntity.ok(messagingService.sendMessage(req.getFromUserId(), req.getToUserId(), req.getContent()));
    }

    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam String userA,
                                             @RequestParam String userB,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(messagingService.getConversation(userA, userB, page, size));
    }
}
