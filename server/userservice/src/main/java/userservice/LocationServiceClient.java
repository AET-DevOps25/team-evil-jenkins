package userservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import model.*;

@Service
public class LocationServiceClient {
    private final RestTemplate restTemplate;
    private final String locationServiceUrl;

    // Inject the base URL from your application.properties
    public LocationServiceClient(RestTemplate restTemplate, @Value("${location.service.url}") String locationServiceUrl) {
        this.restTemplate = restTemplate;
        this.locationServiceUrl = locationServiceUrl;
    }

    // public LocationDTO updateLocation(String userId, double latitude, double longitude) {
    //     String url = UriComponentsBuilder.fromHttpUrl(locationServiceUrl)
    //             .path("/location/update")
    //             .queryParam("userId", userId)
    //             .queryParam("latitude", latitude)
    //             .queryParam("longitude", longitude)
    //             .toUriString();

    //     return restTemplate.postForObject(url, null, Location.class);
    // }

    public List<UserDTO> searchPartnerByArea(String userId, double radius) {
        String url = locationServiceUrl + "/location/nearby?userId={userId}&radius={radius}";

        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<UserDTO>>() {},
            userId,
            radius
        );

        return response.getBody();
    }
}

