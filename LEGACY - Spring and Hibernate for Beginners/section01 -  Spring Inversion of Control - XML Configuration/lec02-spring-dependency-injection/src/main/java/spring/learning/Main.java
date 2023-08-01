package spring.learning;

import spring.learning.coach.Coach;
import spring.learning.coach.impl.BaseballCoach;
import spring.learning.coach.impl.TennisCoach;
import spring.learning.coach.impl.TrackCoach;
import spring.learning.service.FortuneService;
import spring.learning.service.HappyFortuneService;

public class Main {
    // old way: it can quickly because cumbersome
    public static void main(String[] args) {
        FortuneService fortuneService = new HappyFortuneService();
        Coach coach1 = new TrackCoach(fortuneService);
        Coach coach2 = new TennisCoach(fortuneService);
        Coach coach3 = new BaseballCoach(fortuneService);

        System.out.println(coach1.geDailyWorkout());
        System.out.println(coach1.getDaylyFortune());

        System.out.println(coach2.geDailyWorkout());
        System.out.println(coach2.getDaylyFortune());

        System.out.println(coach3.geDailyWorkout());
        System.out.println(coach3.getDaylyFortune());

    }
}
