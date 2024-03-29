package TitanLanding.WindSimulation;

import TitanLanding.WindSimulation.WindValues;
import titan.*;

import java.util.Random;

import static java.lang.Math.abs;

public class WindSimulator {
    // list of altitude ranges (decreasing); altitude > baseAltitudes[i]
    private final static double[] baseAltitudes = createAltitudes();

    // list of corresponding wind speed vectors
    private final static Vector3dInterface[] baseSpeeds = createSpeeds();

    // determines how big the range of distribution is; range = x +- (x*variation)
    private final static double variation = 0.1;

    /**
     * returns the expected wind speeds
     *
     * @return a WindValues object with base (norm) values
     */
    public static WindValues getBaseValues() {
        return new WindValues(baseAltitudes, baseSpeeds);
    }

    /**
     * generates stochastic values for the wind speeds based on the variation
     *
     * @return a WindValues object with stochastic values
     */
    public static WindValues generateWindValues() {
        Vector3dInterface[] speeds = new Vector3dInterface[baseSpeeds.length];
        for (int i = 0; i < baseAltitudes.length; i++) {
            speeds[i] = generateSingleValue(baseAltitudes[i]);
        }
        return new WindValues(baseAltitudes, speeds);
    }

    /**
     * generates the random wind speed for a certain altitude up to 300km
     *
     * @param altitude the altitude of the wind in m (300 000 - 0)
     * @return the strength and direction of the wind (as a Vector in m/s)
     */
    public static Vector3dInterface generateSingleValue(double altitude) {
        Random r = new Random();
        double windSpeedX = 0;
        Vector v = new Vector(0, 0, 0);

        for (int i = 0; i < baseAltitudes.length; i++) {
            if(altitude>=baseAltitudes[i]) { //look for altitude range
                windSpeedX = r.nextGaussian() * baseSpeeds[i].getX() * variation + baseSpeeds[i].getX();
                v.setX(windSpeedX);
                //TODO: add weak vertical winds
                return v;
            }
        }

        return null;
    }

    // used meters, 300km-0km
    private static double[] createAltitudes() {
        double[] arr = new double[20];
        arr[0] = 200000;
        arr[1] = 150000;
        for (int i = 2; i < 9; i++) {
            arr[i] = 150000 - (10000 * (i - 1));
        }
        arr[9] = 72000;
        arr[10] = 68000;
        for (int i = 11; i < 17; i++) {
            arr[i] = 150000 - (10000 * (i - 2));
        }
        arr[17] = 6000;
        arr[18] = 700;
        arr[19] = -5; //accuracy reasons
        return arr;
    }

    private static Vector3dInterface[] createSpeeds() {
        Vector3dInterface[] arr = new Vector3dInterface[20];
        Vector v = new Vector(0, 0, 0);
        arr[0] = v.setXandReturnNewVector(200);
        arr[1] = v.setXandReturnNewVector(150);
        arr[2] = v.setXandReturnNewVector(100);
        arr[3] = v.setXandReturnNewVector(100);
        arr[4] = v.setXandReturnNewVector(100);
        arr[5] = v.setXandReturnNewVector(80);
        arr[6] = v.setXandReturnNewVector(65);
        arr[7] = v.setXandReturnNewVector(60);
        arr[8] = v.setXandReturnNewVector(40);
        arr[9] = v.setXandReturnNewVector(20);
        arr[10] = v.setXandReturnNewVector(5);
        arr[11] = v.setXandReturnNewVector(20);
        arr[12] = v.setXandReturnNewVector(37.5);
        arr[13] = v.setXandReturnNewVector(20);
        arr[14] = v.setXandReturnNewVector(15);
        arr[15] = v.setXandReturnNewVector(10);
        arr[16] = v.setXandReturnNewVector(5);    //until 10km
        arr[17] = v.setXandReturnNewVector(1);    //until 6km
        arr[18] = v.setXandReturnNewVector(-1);   //until 700m it blows the other way
        arr[19] = v.setXandReturnNewVector(0.3);  // 0m
        return arr;
    }
}
