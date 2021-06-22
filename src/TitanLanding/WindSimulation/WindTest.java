package TitanLanding.WindSimulation;

import TitanLanding.WindSimulator;
import titan.Vector;
import titan.Vector3dInterface;

public class WindTest {

    public static void main(String[] args) {

        Vector3dInterface v = new Vector(0,0,0);
        v = WindSimulator.generateSingleValue(70000);
        WindValues wv = WindSimulator.generateWindValues();
        System.out.println(v.toString());

        int c=0;
        for (int i=150; i>0; i--) {
            v = WindSimulator.generateSingleValue(i*1000);

            //System.out.println(i+"km: "+v);
            System.out.print(v.getX()+",");
            c++;
        }
        System.out.println();
        System.out.println(c);
    }
}
