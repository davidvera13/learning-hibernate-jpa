package spring.learning.coach.impl;

import spring.learning.coach.Coach;

public class BaseballCoach implements Coach {
    @Override
    public String geDailyWorkout() {
        return "Spend 30 minutes on batting practice";
    }
}
