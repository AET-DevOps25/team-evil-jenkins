package matchingservice.client;

import java.util.Collections;
import java.util.List;

import matchingservice.dto.Candidate;
import matchingservice.dto.MatchRequest;
import matchingservice.dto.MatchResponse;
import matchingservice.dto.RankedMatchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GenAiClient {

    /**
     * Temporary compatibility shim for existing unit tests.
     */
    @Deprecated
    public List<String> getRankedIds(String userProfile, List<Candidate> candidates) {
        return getRankedMatches(userProfile, candidates).stream().map(RankedMatchDTO::id).toList();
    }

    private final WebClient webClient;

    public GenAiClient(WebClient.Builder builder,
                       @Value("${genai.base-url:http://genai:8000}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    /**
     * Call GenAI service to rank candidate users for a given user profile.
     *
     * @param userProfile Natural-language profile of the active user
     * @param candidates  List of candidate DTOs (id + profile)
     * @return ordered list of candidate IDs (best match first); empty list on error
     */
    public List<RankedMatchDTO> getRankedMatches(String userProfile, List<Candidate> candidates) {
        MatchRequest request = new MatchRequest(userProfile, candidates);
        try {
            MatchResponse response = webClient.post()
                    .uri("/genai/match")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(MatchResponse.class)
                    .block();

            return response != null ? response.matches() : Collections.emptyList();
        } catch (Exception ex) {
            // In production use a proper logger
            System.err.println("Error calling GenAI service: " + ex.getMessage());
            return Collections.emptyList();
        }
    }
}
