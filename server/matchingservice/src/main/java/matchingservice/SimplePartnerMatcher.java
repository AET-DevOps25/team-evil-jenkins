package matchingservice;

import org.springframework.stereotype.Component;

import model.IPartnerMatcher;
import model.User;
import model.Sport;

@Component
public class SimplePartnerMatcher implements IPartnerMatcher {

    @Override
    public User findPartner(User user, Sport sport) {
        // Dummy partner matching implementation
        return null;//new User();
    }
}
