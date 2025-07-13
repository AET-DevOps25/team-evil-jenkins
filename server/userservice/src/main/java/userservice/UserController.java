package userservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
@Tag(name = "User API", description = "API for managing users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.userMapper = mapper;
    }

    @Operation(summary = "Get a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of user to be searched") @PathVariable("id") String id) {
                User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(userMapper.toDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Invalid user supplied",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class))))
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> dtos = users.stream()
                                          .map(userMapper::toDTO)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@Parameter(description = "ID of user to be deleted") @PathVariable("id") String id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{id}/nearby")
    public ResponseEntity<List<UserDTO>> getNearbyUsers( //TODO: Use UserMapper ???
            @PathVariable("id") String id,
            @RequestParam double radius) {
        List<User> users = userService.findNearbyUsers(id, radius);
        List<UserDTO> dtos = users.stream()
        .map(user -> new UserDTO(user.getId(), user.getName()))
        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    //TODO: Login endpoint
}
