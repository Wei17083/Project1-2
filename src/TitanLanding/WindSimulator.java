package TitanLanding;

import titan.*;
import java.util.Random;

public class WindSimulator {
    // list of altitude ranges; [i-1] < altitude <= [i]
    private final static int[] baseAltitudes = createAltitudes();

    // list of corresponding wind speed vectors

    private final static Vector3dInterface[] baseSpeeds = createSpeeds();

    // determines how big the range of distribution is; range = x +- (x*variation)

    private final static double variation = 0.1;
    /**
     * returns the expected wind speeds
     * @return a WindValues object with base (norm) values
     */
    public static WindValues getBaseValues(){
        return new WindValues(baseAltitudes, baseSpeeds);
    }
    /**
     * generates stochastic values for the wind speeds based on the variation
     * @return a WindValues object with random values
     */
    public static WindValues generateWindValues(){
        return null;
    }

    /**
     * generates the random wind speed for a certain altitude
     * @param altitude the altitude of the wind
     * @return the strength and direction of the wind
     */
    public static Vector3dInterface generateSingleValue(int altitude){
        Random r = new Random();
        double answer = 0;
        Vector v = new Vector(0,0,0);

        for (int i=0; i<baseAltitudes.length-1; i++) {
            if (baseAltitudes[i+1]<altitude && altitude<=baseAltitudes[i]) {
                answer = r.nextGaussian()*variation+baseSpeeds[i+1].getX();
                v.setXandReturnNewVector(answer);
                return v;
            }
        }

        return null;
    }

        // used meters, 200km-0km
    private static int[] createAltitudes() {
        int[] arr = new int[19];
        arr[0]=200000;
        arr[1]=150000;
        for (int i = 2; i < 9; i++) {
            arr[i]= 150000 - (10000 * (i-1));
        }
        arr[9]=72000;
        arr[10]=68000;
        for (int i = 11; i < 17; i++) {
            arr[i]= 150000 - (10000 * (i-2));
        }
        arr[17] = 100;
        arr[18] = 0;
        return arr;
    }

    private static Vector3dInterface[] createSpeeds() {
        Vector3dInterface[] arr = new Vector3dInterface[19];
        Vector v = new Vector(0,0,0);
        arr[0]=v.setXandReturnNewVector(-200); //direction missing
        arr[1]=v.setXandReturnNewVector(-1); //value and direction missing
        arr[2]=v.setXandReturnNewVector(100);
        arr[3]=v.setXandReturnNewVector(100);
        arr[4]=v.setXandReturnNewVector(100);
        arr[5]=v.setXandReturnNewVector(80);
        arr[6]=v.setXandReturnNewVector(65);
        arr[7]=v.setXandReturnNewVector(60);
        arr[8]=v.setXandReturnNewVector(40);
        arr[9]=v.setXandReturnNewVector(20);
        arr[10]=v.setXandReturnNewVector(5);
        arr[11]=v.setXandReturnNewVector(20);
        arr[12]=v.setXandReturnNewVector(37.5);
        arr[13]=v.setXandReturnNewVector(20);
        arr[14]=v.setXandReturnNewVector(15);
        arr[15]=v.setXandReturnNewVector(10);
        arr[16]=v.setXandReturnNewVector(5);
        arr[17]=v.setXandReturnNewVector(1);    //100m
        arr[18]=v.setXandReturnNewVector(0.3);  // 0m

        return arr;
    }
}
