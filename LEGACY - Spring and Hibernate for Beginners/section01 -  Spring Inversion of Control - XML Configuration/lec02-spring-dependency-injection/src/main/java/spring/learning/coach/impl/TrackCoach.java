package spring.learning.coach.impl;

import spring.learning.coach.Coach;
import spring.learning.service.FortuneService;

public class TrackCoach implements Coach {
    // define a private field for the dependency
    private final FortuneService fortuneService;

    // define a constructor for dependency injection
    public TrackCoach(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }


    @Override
    public String geDailyWorkout() {
        return "Run a hard 5km";
    }

    @Override
    public String getDaylyFortune() {
        return "Track Coach >> " + fortuneService.getFortune();
    }
}
