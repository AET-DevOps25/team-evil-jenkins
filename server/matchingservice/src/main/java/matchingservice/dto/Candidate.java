package matchingservice.dto;

import java.util.List;

public record Candidate(String id, String name, List<String> sportInterests) {
}
