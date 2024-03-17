package be.howest.ti.mars.logic.domain;

public class Recommendation {
    private final int recommendationID;
    private final String activityName;

    public Recommendation(int recommendationID, String activityName) {
        this.recommendationID = recommendationID;
        this.activityName = activityName;
    }

    public int getRecommendationID() {
        return recommendationID;
    }

    public String getActivityName() {
        return activityName;
    }
}
