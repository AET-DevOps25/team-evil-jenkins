package userservice;

import model.DTOEntityMapper;
import model.UserDTO;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DTOEntityMapper<User, UserDTO> {

    private Map<String, List<String>> availabilityToMap(Set<AvailabilitySlot> slots) {
        if (slots == null) return null;
        Map<String, List<String>> map = new HashMap<>();
        slots.forEach(s -> map.computeIfAbsent(s.getDay(), k -> new java.util.ArrayList<>()).add(s.getSlot()));
        return map;
    }

    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getId(),
                entity.getName(),
                entity.getPicture(),
                entity.getBio(),
                entity.getSkillLevel(),
                availabilityToMap(entity.getAvailability()),
                entity.getSportInterests());
    }

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User(dto.id(), dto.name());
        user.setPicture(dto.picture());
        user.setBio(dto.bio());
        user.setSkillLevel(dto.skillLevel());
        if (dto.availability() != null) {
            java.util.Set<AvailabilitySlot> slots = new java.util.HashSet<>();
            dto.availability().forEach((day, list) -> list.forEach(slot -> slots.add(new AvailabilitySlot(day, slot))));
            user.setAvailability(slots);
        }
        user.setSportInterests(dto.sportInterests());
        return user;
    }

}
