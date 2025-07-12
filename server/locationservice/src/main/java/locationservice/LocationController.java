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


@RestController
@RequestMapping("/location")
@Tag(name = "Location API", description = "Endpoints for updating and searching user locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Operation(
        summary = "Update or create a user's location",
        description = "Updates or creates the current location of a user identified by id and name",
        responses = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully",
                content = @Content(schema = @Schema(implementation = LocationEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
        }
    )
    @PostMapping("/update")
    public ResponseEntity<LocationEntity> updateLocation(
            @Parameter(description = "ID of the user") @RequestParam String id,
            @Parameter(description = "Name of the user") @RequestParam String name,
            @Parameter(description = "Latitude coordinate") @RequestParam double latitude,
            @Parameter(description = "Longitude coordinate") @RequestParam double longitude) {
        LocationEntity updated = locationService.updateLocation(id, name, latitude, longitude);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Get all locations",
        description = "Returns all location records in the database",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of locations",
                content = @Content(schema = @Schema(implementation = LocationEntity.class)))
        }
    )
    @GetMapping("/all")
    public ResponseEntity<java.util.List<LocationEntity>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAll());
    }

    @Operation(
        summary = "Get location by user ID",
        description = "Returns the location of a user by their ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Location found",
                content = @Content(schema = @Schema(implementation = LocationEntity.class))),
            @ApiResponse(responseCode = "404", description = "Location not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<LocationEntity> getLocationById(
            @Parameter(description = "ID of the user") @PathVariable String id) {
        return locationService.getLocation(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Find locations near a given point within a radius",
        description = "Returns a list of locations within the specified radius (in kilometers) of the given coordinates",
        responses = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(schema = @Schema(implementation = LocationEntity.class)))
        }
    )
    @GetMapping("/nearby")
    public ResponseEntity<java.util.List<LocationEntity>> searchNearby(
            @Parameter(description = "Latitude coordinate") @RequestParam double latitude,
            @Parameter(description = "Longitude coordinate") @RequestParam double longitude,
            @Parameter(description = "Search radius in kilometers") @RequestParam double radius) {
        return ResponseEntity.ok(locationService.searchWithinRadius(latitude, longitude, radius));
    }
}

