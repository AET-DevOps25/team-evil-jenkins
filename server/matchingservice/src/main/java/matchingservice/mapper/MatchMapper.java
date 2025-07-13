package matchingservice.mapper;

import java.util.List;
import model.MatcherDTO;
import matchingservice.entity.Match;

public final class MatchMapper {
    private MatchMapper() {}

    public static MatcherDTO toDto(Match m) {
        return new MatcherDTO(
                m.getMatchedUserId(),
                m.getScore(),
                m.getReason(),
                m.getCommonPreferences() != null ? List.of(m.getCommonPreferences().split(",")) : List.of(),
                m.getCreatedAt()
        );
    }
}
