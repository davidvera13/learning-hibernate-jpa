package spring.learning.coach.impl;

import spring.learning.coach.Coach;
import spring.learning.service.FortuneService;

public class TennisCoach implements Coach {
    // define a private field for the dependency
    private final FortuneService fortuneService;

    // define a constructor for dependency injection
    public TennisCoach(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @Override
    public String geDailyWorkout() {
        return "Train tennis...";
    }

    @Override
    public String getDaylyFortune() {
        return "Tennis Coach >> " +  fortuneService.getFortune();
    }
}
