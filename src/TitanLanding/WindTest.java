package TitanLanding;

import titan.Vector;
import titan.Vector3dInterface;

public class WindTest {

    public static void main(String[] args) {

        Vector3dInterface v = new Vector(0,0,0);
        v = WindSimulator.generateSingleValue(70000);
        System.out.println(v.toString());
    }
}
