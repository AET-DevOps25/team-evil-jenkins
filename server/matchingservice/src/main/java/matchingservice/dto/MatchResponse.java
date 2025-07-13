package matchingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchResponse(@JsonProperty("matches") List<RankedMatchDTO> matches) {
}
