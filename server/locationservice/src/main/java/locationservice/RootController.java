package locationservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String rootInfo() {
        return "Location Service is running. OpenAPI docs available at /v3/api-docs";
    }
}