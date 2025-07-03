package matchingservice;

// Deprecated dummy matcher; component annotation removed

import model.IPartnerMatcher;
import model.User;
import model.Sport;

public class SimplePartnerMatcher implements IPartnerMatcher {

    @Override
    public User findPartner(User user, Sport sport) {
        // Dummy partner matching implementation
        return null;//new User();
    }
}
