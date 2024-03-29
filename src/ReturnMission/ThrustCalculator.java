package ReturnMission;

import titan.Vector3dInterface;

public class ThrustCalculator {

    private static double Ve = 60000; //m/s
    private static final double mdot_max = 15000; //kg/s
    private static double step_size = 500;

    /**
     * calculates how much fuel (mass) is needed to reach a certain velocity
     * @param M Mass of rocket in kg
     * @param V0 initial velocity
     * @param Vr desired velocity
     * @return mass of fuel needed in kg
     */
    public static double getFuelForVelocity(double M, Vector3dInterface V0, Vector3dInterface Vr){
        double velocityChange = Vr.sub(V0).norm();
        return (M*(velocityChange)/Ve);
    }

    /**
     * calculates how much time is needed to use up fuel
     * @param m mass of fuel to be used in kg
     * @return time it takes to use fuel in s
     */
    public static double convertFuelToTime(double m){
        double t = m/mdot_max;
        return(t);
    }

    /**
     * calculates how many time steps are needed for time to pass
     * @param t time that should pass in s
     * @return how many time steps are needed
     */
    public static double convertTimeToSteps(double t){
        double ts = t/step_size;
        return(ts);
    }



    public static double getVe() {
        return Ve;
    }

    public static double getMdot_max() {
        return mdot_max;
    }

    public static double getStep_size() {
        return step_size;
    }

    public static void setStep_size(double step_size) {
        ThrustCalculator.step_size = step_size;
    }

    public static void setVE(double newVe){
        Ve = newVe;
    }
}