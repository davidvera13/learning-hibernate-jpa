package spring.learning.coach.impl;

import spring.learning.coach.Coach;
import spring.learning.service.FortuneService;

public class BaseballCoach implements Coach {
    // define a private field for the dependency
    private final FortuneService fortuneService;

    // define a constructor for dependency injection
    public BaseballCoach(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @Override
    public String geDailyWorkout() {
        return "Spend 30 minutes on batting practice";
    }

    @Override
    public String getDaylyFortune() {
        return "Baseball Coach >> " + fortuneService.getFortune();
    }


}
