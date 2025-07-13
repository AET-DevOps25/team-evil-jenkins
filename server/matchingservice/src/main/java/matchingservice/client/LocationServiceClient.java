package matchingservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class LocationServiceClient {

    private final WebClient webClient;

    public record LocationDto(String id, String name, double latitude, double longitude) {}

    public LocationServiceClient(WebClient.Builder builder,
                                 @Value("${locationservice.base-url:http://location-service:8080}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public LocationDto getLocation(String userId) {
        try {
            return webClient.get()
                    .uri("/location/{id}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(LocationDto.class)
                    .onErrorResume(err -> Mono.empty())
                    .block();
        } catch (Exception ex) {
            System.err.println("Error calling location-service: " + ex.getMessage());
            return null;
        }
    }
}
