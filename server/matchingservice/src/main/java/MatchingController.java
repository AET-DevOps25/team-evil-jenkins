package matchingservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/matching")
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    @GetMapping("/find/{userId}")
    public ResponseEntity<String> findMatch(@PathVariable String userId) {
        return ResponseEntity.ok(matchingService.findMatch(userId));
    }
}
