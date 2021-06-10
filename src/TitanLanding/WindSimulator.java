package TitanLanding;

import titan.*;

import java.util.Random;

import static java.lang.Math.abs;

public class WindSimulator {
    // list of altitude ranges; [i-1] < altitude <= [i]
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
     * generates the random wind speed for a certain altitude
     *
     * @param altitude the altitude of the wind
     * @return the strength and direction of the wind
     */
    public static Vector3dInterface generateSingleValue(double altitude) { // add 4 different cases; 60-0,72-60,130-72, over 130
        Random r = new Random();
        double windSpeedX = 0;
        Vector v = new Vector(0, 0, 0);

        // case 1, over 130km, fluctuates at 100
        if ((130000 <= altitude && 200000 > altitude)) {
            windSpeedX = r.nextGaussian() * 100 * variation + 100; // bigger speed, more variation
            v.setX(windSpeedX);
            return v;
            }
        // other cases
        else if ((0<=altitude && 130000>altitude)){
            double difOfAltitude = 0;

            for (int i = 0; i < baseAltitudes.length-1; i++) { //starts from 200km to 0km
                if (altitude > baseAltitudes[i+1] && altitude <= baseAltitudes[i]) {
                    double midAltitude = (baseAltitudes[i+1] + baseAltitudes[i]) / 2;
                    double speed = baseSpeeds[i+1].getX();

                    if ((0 <= altitude && 60000 > altitude) || (72000 <= altitude && 130000 > altitude)) { // for 60-0, 120-72, increases when altitude increases
                        difOfAltitude = (altitude-midAltitude) / 1000; // for each 1k altitude difference speed will +/- by 1
                    }
                    else { // for 60km-72km, decreases when altitude increases
                        difOfAltitude = -((altitude - midAltitude) / 1000); // only difference is that here, there is "-"
                    }

                    windSpeedX = r.nextGaussian() * speed * variation + speed + difOfAltitude; // bigger speed, more variation
                    v.setX(windSpeedX);
                    return v;
                }
            }
        }
        return null;
    }

    // used meters, 200km-0km
    private static double[] createAltitudes() {
        double[] arr = new double[19];
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
        arr[17] = 100;
        arr[18] = 0;
        return arr;
    }

    private static Vector3dInterface[] createSpeeds() {
        Vector3dInterface[] arr = new Vector3dInterface[19];
        Vector v = new Vector(0, 0, 0);
        arr[0] = v.setXandReturnNewVector(-200); //direction missing
        arr[1] = v.setXandReturnNewVector(-1); //value and direction missing
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
        arr[16] = v.setXandReturnNewVector(5);    //10km
        arr[17] = v.setXandReturnNewVector(1);    //100m
        arr[18] = v.setXandReturnNewVector(0.3);  // 0m

        return arr;
    }
}
