package ReturnMission;

import titan.BodyList;

public class ReturnMission {
    public final double GRAV_CONSTANT = 6.674E-11;
    final double velocityOrbit;
    final double radius = BodyList.getBodyList()[8].getRadius()+200000;
    final double stepsize = 500;

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
    }



}
