package matchingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RankedMatchDTO(
        @JsonProperty("id") String id,
        @JsonProperty("score") Double score,
        @JsonProperty("explanation") String explanation,
        @JsonProperty("common_preferences") List<String> commonPreferences
) {}
