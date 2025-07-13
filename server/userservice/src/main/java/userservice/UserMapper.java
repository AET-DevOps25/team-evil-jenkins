package userservice;

import model.DTOEntityMapper;
import model.UserDTO;
import org.springframework.stereotype.Component; 

@Component 
public class UserMapper implements DTOEntityMapper<User, UserDTO>{

    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(entity.getId(), entity.getName());
    }

    @Override
    public User toEntity(UserDTO dto) {
        return new User(dto.id(), dto.name());
    }
    
}
