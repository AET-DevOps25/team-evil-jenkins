package user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class DummyController {
    @GetMapping(value = "/user", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello user");
    }

}
