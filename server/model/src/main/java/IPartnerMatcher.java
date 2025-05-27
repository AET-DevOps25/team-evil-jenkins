package model;

public abstract class IPartnerMatcher {
    public double compatibilityScore;

    public double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    //TODO: Implement partner matching logic 
    public abstract User findPartner(User user, Sport sport);
}
