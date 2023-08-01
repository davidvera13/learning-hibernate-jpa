package spring.learning;

import spring.learning.coach.impl.BaseballCoach;
import spring.learning.coach.Coach;
import spring.learning.coach.impl.TrackCoach;

public class MainApp {
    public static void main(String[] args) {
        // in classic java: instantiate object and call methods
        BaseballCoach baseballCoach = new BaseballCoach();
        System.out.println(baseballCoach.geDailyWorkout());

        // make more generic: code to interface
        Coach coach1 = new BaseballCoach();
        System.out.println(coach1.geDailyWorkout());

        // handle differnt implementation ...
        TrackCoach trackCoach = new TrackCoach();
        System.out.println(trackCoach.geDailyWorkout());

        // using interface allow to use any kind of coach ...
        Coach coach2 = new TrackCoach();
        System.out.println(coach2.geDailyWorkout());
    }
}