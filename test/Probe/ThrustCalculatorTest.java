package Probe;

import ReturnMission.ThrustCalculator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ThrustCalculatorTest {
    private static final double Ve = 20000;
    private static final double mdot_max = 15000;
    private double step_size = 1;

//    @Test
//    void getFuelForVelocity() {
//        assertEquals(0.5, ThrustCalculator.getFuelForVelocity(100,10,110));
//    }

    @Test
    void convertFuelToTime() {
        assertEquals(2,ThrustCalculator.convertFuelToTime(mdot_max*2));
    }

    @Test
    void convertTimeToSteps() {
        ThrustCalculator.setStep_size(2);
        assertEquals(500,ThrustCalculator.convertTimeToSteps(1000));
    }

    @Test
    void getVe() {
        assertEquals(Ve,ThrustCalculator.getVe());
    }

    @Test
    void getMdot_max() {
        assertEquals(mdot_max,ThrustCalculator.getMdot_max());
    }

    @Test
    void getStep_size() {
        ThrustCalculator.setStep_size(step_size);
        assertEquals(step_size,ThrustCalculator.getStep_size());
    }

    @Test
    void setStep_size() {
        ThrustCalculator.setStep_size(2);
        assertEquals(2,ThrustCalculator.getStep_size());
    }
}