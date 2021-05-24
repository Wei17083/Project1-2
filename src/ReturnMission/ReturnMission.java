package ReturnMission;

import titan.BodyList;
import titan.Vector;
import titan.Vector3dInterface;

public class ReturnMission {
    public final double GRAV_CONSTANT = 6.674E-11;
    final double velocityOrbit;
    final double radius = BodyList.getBodyList()[8].getRadius()+200000;
    final double stepsize = 500;
    final Vector3dInterface INITIAL_VELOCITY_ESTIMATE = new Vector(33573.34843239226, -69124.95246248483, -703.6818444149116);

    public ReturnMission(){
        velocityOrbit = velocityOrbit();
    }
    public double velocityOrbit(){
        return  Math.sqrt((GRAV_CONSTANT* BodyList.getBodyList()[8].getMass())/radius);
    }

    public double timeOrbit(){
        double circumference = 2*Math.PI*radius;
        return circumference/velocityOrbit;
    }


    public static void main(String[] args){
        ReturnMission r = new ReturnMission();
        System.out.println("velocity "+r.velocityOrbit());
        System.out.println("radius "+r.radius);
        System.out.println("mass "+BodyList.getBodyList()[8].getMass());
        System.out.println("time "+r.timeOrbit());

        /* TODO
            1: calculate launch velocity with correct solver (do Newton rhapson)
            2: have loop or something to handle launch (first x steps)
            3: at certain intervals call newton rhapson and adjust trajectory to reach titan
            4: slow down to orbit titan
            5: orbit titan a few times
            6: Use newton rhapson to find velocity to return to earth
            7: escape from orbit of titan
            8: same as step 3 but with goal Earth
            9: crash into earth
         */

    }



}
