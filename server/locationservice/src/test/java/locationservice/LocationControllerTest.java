package locationservice;

import model.LocationDTO;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private LocationMapper locationMapper;

    @Test
    void testUpdateLocation() throws Exception {
        String userId = "user1";
        double latitude = 48.13;
        double longitude = 11.57;
        Location location = new Location(userId, latitude, longitude );
        
        when(locationService.updateLocation(userId, latitude, longitude)).thenReturn(location);
        
        when(locationMapper.toDTO(location)).thenReturn(new LocationDTO(userId, latitude, longitude));

        mockMvc.perform(post("/location/update")
                        .param("userId", userId) 
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.latitude").value(latitude))
                .andExpect(jsonPath("$.longitude").value(longitude));
    }

    @Test
    void testGetAllLocations() throws Exception {
        List<Location> list = List.of(new Location("u1", 0.0, 0.0),
                                           new Location("u2", 1.0, 1.0));
        when(locationService.getAll()).thenReturn(list);

        mockMvc.perform(get("/location/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("u1"))
                .andExpect(jsonPath("$[1].id").value("u2"));
    }

    @Test
    void testGetLocationById() throws Exception {
        Location alice = new Location("user1", 48.13, 11.57);
        when(locationService.getLocation("user1")).thenReturn(alice);

        mockMvc.perform(get("/location/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    public void testSearchPartnerByArea() throws Exception {
        String userId = "user1";
        double radius = 10.0;
        List<String> nearbyUserIds = Arrays.asList("user2", "user3");
        
        when(locationService.searchPartnerByArea(userId, radius)).thenReturn(nearbyUserIds);

        mockMvc.perform(get("/location/nearby")
                        .param("latitude", "48.13")
                        .param("longitude", "11.57")
                        .param("radius", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("u1"));
    }
}

