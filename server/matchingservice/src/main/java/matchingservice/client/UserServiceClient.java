package matchingservice.client;

import java.util.Collections;
import java.util.List;

import model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder builder,
                             @Value("${userservice.base-url:http://user-service:8080}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public User getUser(String userId) {
        try {
            return webClient.get()
                    .uri("/user/{id}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
        } catch (Exception ex) {
            System.err.println("Error fetching user: " + ex.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return webClient.get()
                    .uri("/user")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .collectList()
                    .block();
        } catch (Exception ex) {
            System.err.println("Error fetching users: " + ex.getMessage());
            return Collections.emptyList();
        }
    }
}
