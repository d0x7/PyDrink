package com.tomschammo.pydrink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Addition {

    private double height;
    private double weight;
    private double minute;
    private List<Double> timeList = new ArrayList<>();
    private double eliminationRate = 0.018;

    public Addition(double height, double weight, int minute) {
        this.height = height;
        this.weight = weight;
        this.minute = minute;
    }

    public static void main(String[] args) {
        Addition add = new Addition(170, 60, 800);
        List<Double> a = add.totalConsumed(330, 20, 10, 12, true);
        a = add.eliminateAlc(a);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }
    }

    // adds the drink
    private double drink(int volume, double percent, int minutes, double halfLife) {
        double temp = minutes / halfLife; // find how many halflives passed
        return (volume / 1000d) * (percent / 100d) - (volume / 1000d) * (percent / 100d) * Math.pow(0.5, temp); // find alcohol is absorbed
    }

    // creates an array containing the total amount of alcohol absorbed by the body
    public List<Double> totalConsumed(int volume, double percent, int time, double halfLife, boolean gender) {
        List<Double> tempTimeList = new ArrayList<>();
        time = -time;

        while (tempTimeList.size() < TimeUnit.DAYS.toMinutes(1) + minute) {
            double absoluteConcentration = drink(volume, percent, time, halfLife);
            tempTimeList.add(Util.calculateBAC(absoluteConcentration, weight, height, gender));
            time++;
        }
        if (timeList.size() == 0) {
            timeList = tempTimeList;
        } else {       // add lists together
            for (int i = 0; i < timeList.size(); i++) {
                timeList.set(i, timeList.get(i) + tempTimeList.get(i));
            }
        }
        return timeList;
    }

    // creates an array of the amount of blood in the alcohol (based on the 'totalConsumed method'
    public List<Double> eliminateAlc(List<Double> timeList) {
        List<Double> eliminationList = new ArrayList<Double>();
        eliminationList.add(0d); // add 0 as starting point
        for (int i = 0; i < timeList.size(); i++) {
            // if there is alcohol in the blood, eleminate one minute worth of alcohol by increasing
            // the value appended in eleminationList, otherwise append the same value (e.g. don't eliminate
            // alcohol since the is none)
            if (timeList.get(i) - eliminationList.get(i) / 60 * eliminationRate > 0) {
                eliminationList.add(eliminationList.get(i) + 1);
            } else {
                eliminationList.add(eliminationList.get(i));
            }
        }
        eliminationList.remove(0); // remove the inital 0 stored in eliminationList
        for (int i = 0; i < timeList.size(); i++) {
            eliminationList.set(i, eliminationList.get(i) / 60 * eliminationRate);
            eliminationList.set(i, Math.max(0d, timeList.get(i) - eliminationList.get(i)));
        }
        return eliminationList;
    }

    public int timeUntilSober(ArrayList<Double> bacList, int currentTime) {
        for (int i = bacList.size(); i < currentTime; i--) {
            if (bacList.get(i) <= 0) return i;
        }
        return 0;
    }
}
