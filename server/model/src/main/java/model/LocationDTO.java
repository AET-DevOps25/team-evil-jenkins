package model;

import java.time.Instant;

public record LocationDTO(String userId, double latitude, double longitude, Instant timestamp) {
}
