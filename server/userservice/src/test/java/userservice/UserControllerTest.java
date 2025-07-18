package userservice;

import userservice.UserController;
import userservice.UserService;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        User user = new User("1", "Alice");
        when(userService.getUserById("1")).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById("2")).thenReturn(null);

        mockMvc.perform(get("/user/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddUser() throws Exception {
        UserDTO user = new UserDTO("1", "Alice", "", "", "", java.util.Map.of(), new ArrayList<>());

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"name\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User added successfully"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User("1", "Alice"), new User("2", "Bob"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    public void testDeleteUserById() throws Exception {
        when(userService.deleteUserById("1")).thenReturn(true);
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    public void testDeleteUserById_NotFound() throws Exception {
        when(userService.deleteUserById("2")).thenReturn(false);

        mockMvc.perform(delete("/user/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}