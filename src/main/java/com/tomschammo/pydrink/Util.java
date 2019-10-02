package com.tomschammo.pydrink;

public class Util {

    private final static double widmarkFactorMale = 1.0181D - 0.01213D;
    private final static double widmarkFactorFemale = 0.9367D - 0.01240D;
    private final static double alcoholDensity = 0.78974D;

    /**
     * Calculates the BMI using the given values
     *
     * @param weight weight in kg
     * @param height height in cm
     * @return bmi of the given values
     */
    public static double calculateBMI(double weight, double height) {
        return weight / Math.pow(height / 100D, 2);
    }

    /**
     * Calculates the widmark factor using the given values
     *
     * @param weight weight in kg
     * @param height height in cm
     * @param gender true for male, false for female
     * @return widmark factor of the given values
     */
    public static double calculateWidmarkFactor(double weight, double height, boolean gender) {
        return calculateWidmarkFactor(calculateBMI(weight, height), gender);
    }

    /**
     * Calculates the widmark factor using the given values
     *
     * @param bmi body mass index
     * @param gender true for male, false for female
     * @return widmark factor of the given values
     */
    public static double calculateWidmarkFactor(double bmi, boolean gender) {
        return (gender ? widmarkFactorMale : widmarkFactorFemale) * bmi;
    }

    /**
     * Calculates the blood alcohol concentration for the given values
     *
     * @param absoluteConcentration absolute concentration of blood
     * @param weight weight in kg
     * @param height height in cm
     * @param gender true for male, false for female
     * @return blood alcohol concentration of the given values
     */
    public static Double calculateBAC(double absoluteConcentration, double weight, double height, boolean gender) {
        return calculateBAC(absoluteConcentration, calculateWidmarkFactor(weight, height, gender), weight);
    }

    /**
     * Calculates the blood alcohol concentration for the given values
     *
     * @param absoluteConcentration absolute concentration of blood
     * @param widmarkFactor widmark factor
     * @param weight weight in kg
     * @return blood alcohol concentration of the given values
     */
    public static double calculateBAC(double absoluteConcentration, double widmarkFactor, double weight) {
        return Math.max((100D * absoluteConcentration * alcoholDensity) / (widmarkFactor * weight), 0);
    }
}
