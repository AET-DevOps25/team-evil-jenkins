package locationservice;

import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    public void testUpdateLocation() throws Exception {
        String userId = "user1";
        double latitude = 48.137154;
        double longitude = 11.576124;
        Location location = new Location(userId, latitude, longitude, Instant.now());
        
        when(locationService.updateLocation(userId, latitude, longitude)).thenReturn(location);

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
    public void testSearchPartnerByArea() throws Exception {
        String userId = "user1";
        double radius = 10.0;
        List<String> nearbyUserIds = Arrays.asList("user2", "user3");
        
        when(locationService.searchPartnerByArea(userId, radius)).thenReturn(nearbyUserIds);

        mockMvc.perform(get("/location/nearby")
                .param("userId", userId)
                .param("radius", String.valueOf(radius)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value("user2"))
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[1].id").value("user3"))
                .andExpect(jsonPath("$[1].name").value("Charlie"));
    }
}
