package matchingservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// Candidate now includes location
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchRequest(@JsonProperty("user_profile") String userProfile,
                           List<Candidate> candidates) {}
