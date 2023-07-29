package spring.learning.coach.impl;

import spring.learning.coach.Coach;

public class TrackCoach implements Coach {
    @Override
    public String geDailyWorkout() {
        return "Run a hard 5km";
    }
}
