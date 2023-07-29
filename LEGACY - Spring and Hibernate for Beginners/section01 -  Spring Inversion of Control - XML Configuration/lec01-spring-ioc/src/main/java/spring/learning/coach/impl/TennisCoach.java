package spring.learning.coach.impl;

import spring.learning.coach.Coach;

public class TennisCoach implements Coach {
    @Override
    public String geDailyWorkout() {
        return "Train tennis...";
    }
}
