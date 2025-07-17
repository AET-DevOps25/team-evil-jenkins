package matchingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// Candidate now includes location
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchRequest(@JsonProperty("user") Candidate user,
                           List<Candidate> candidates) {}
