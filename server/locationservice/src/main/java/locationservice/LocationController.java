package locationservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import model.Location;
import model.User;

@RestController
@RequestMapping("/location")
@Tag(name = "Location API", description = "Endpoints for updating and searching user locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Operation(
        summary = "Update a user's location",
        description = "Updates the current location of a user identified by userId",
        responses = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully",
                content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
        }
    )
    @PostMapping("/update")
    public ResponseEntity<Location> updateLocation(
            @Parameter(description = "ID of the user") @RequestParam String userId,
            @Parameter(description = "Latitude coordinate") @RequestParam double latitude,
            @Parameter(description = "Longitude coordinate") @RequestParam double longitude) {
        Location updated = locationService.updateLocation(userId, latitude, longitude);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Find users near a given user within a radius",
        description = "Returns a list of users within the specified radius (in kilometers) of the given user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping("/nearby")
    public ResponseEntity<List<User>> searchPartnerByArea(
            @Parameter(description = "ID of the user") @RequestParam String userId,
            @Parameter(description = "Search radius in kilometers") @RequestParam double radius) {
        List<User> users = locationService.searchPartnerByArea(userId, radius);
        return ResponseEntity.ok(users);
    }
}
