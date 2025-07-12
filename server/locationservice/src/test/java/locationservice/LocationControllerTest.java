package locationservice;

import locationservice.LocationEntity;
import locationservice.LocationService;
import locationservice.LocationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void testUpdateLocation() throws Exception {
        LocationEntity alice = new LocationEntity("user1", "Alice", 48.13, 11.57);
        when(locationService.updateLocation("user1", "Alice", 48.13, 11.57)).thenReturn(alice);

        mockMvc.perform(post("/location/update")
                        .param("id", "user1")
                        .param("name", "Alice")
                        .param("latitude", "48.13")
                        .param("longitude", "11.57"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user1"))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.latitude").value(48.13))
                .andExpect(jsonPath("$.longitude").value(11.57));
    }

    @Test
    void testGetAllLocations() throws Exception {
        List<LocationEntity> list = List.of(new LocationEntity("u1", "A", 0.0, 0.0),
                                           new LocationEntity("u2", "B", 1.0, 1.0));
        when(locationService.getAll()).thenReturn(list);

        mockMvc.perform(get("/location/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("u1"))
                .andExpect(jsonPath("$[1].id").value("u2"));
    }

    @Test
    void testGetLocationById() throws Exception {
        LocationEntity alice = new LocationEntity("user1", "Alice", 48.13, 11.57);
        when(locationService.getLocation("user1")).thenReturn(java.util.Optional.of(alice));

        mockMvc.perform(get("/location/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testSearchNearby() throws Exception {
        List<LocationEntity> nearby = List.of(new LocationEntity("u1", "A", 48.13, 11.57));
        when(locationService.searchWithinRadius(48.13, 11.57, 5.0)).thenReturn(nearby);

        mockMvc.perform(get("/location/nearby")
                        .param("latitude", "48.13")
                        .param("longitude", "11.57")
                        .param("radius", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("u1"));
    }
}

