package matchingservice;

// Deprecated dummy matcher; component annotation removed

import model.IPartnerMatcher;
import model.UserDTO;
import model.SportDTO;

public class SimplePartnerMatcher implements IPartnerMatcher {

    @Override
    public UserDTO findPartner(UserDTO user, SportDTO sport) {
        // Dummy partner matching implementation
        return null;//new User();
    }
}
