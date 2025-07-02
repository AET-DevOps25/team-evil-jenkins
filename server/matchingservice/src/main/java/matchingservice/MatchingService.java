package matchingservice;

import org.springframework.stereotype.Service;

import model.User;
import model.IPartnerMatcher;

@Service
public class MatchingService {

    private final IPartnerMatcher partnerMatcher;

    public MatchingService(IPartnerMatcher partnerMatcher) {
        this.partnerMatcher = partnerMatcher;
    }

    public User findPartner(String userId) {
        // TODO: fetch user from repository/service, currently dummy implementation
        return null;//partnerMatcher.findPartner(new User(), null);
    }
}
