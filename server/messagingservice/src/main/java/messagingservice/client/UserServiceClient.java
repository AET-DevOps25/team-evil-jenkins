package messagingservice.client;

import model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import java.util.Collections;
import java.util.List;

@Component
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(@Value("${user.service.url:http://user-service:8080}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public UserDTO getUser(String id) {
        try {
            return webClient.get()
                    .uri("/user/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (Exception ex) {
            System.err.println("Error fetching user " + id + ": " + ex.getMessage());
            return null;
        }
    }

    public List<UserDTO> getUsers(List<String> ids) {
        // No dedicated batch endpoint; fall back to serial fetch
        return ids == null ? Collections.emptyList()
                : ids.stream()
                        .map(this::getUser)
                        .filter(u -> u != null)
                        .toList();
    }
}
