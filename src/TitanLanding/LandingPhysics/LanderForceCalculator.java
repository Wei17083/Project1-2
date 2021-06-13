package TitanLanding.LandingPhysics;

import TitanLanding.Lander.*;
import titan.*;

public class LanderForceCalculator {

    public static Vector3dInterface calculateGravityForce(Lander lander){

        double gravConst = -1.352; // m/s²

        double landerMass = lander.getMASS();
        double titanMass = 1.34553e23;

        double titanRadius = 2.5755e6;
        double landerAltitude = lander.getState().getPosition().getY();

        double separation = titanRadius + landerAltitude;

        double gravY = (gravConst * landerMass * titanMass) / (Math.pow(separation, 2));

        return new Vector(0, gravY, 0);
    }

    public static Vector3dInterface calculateWindForce(Lander lander, Vector3dInterface windVelocity){

        double dragCoefficient = 0.09;
        double airDensity = 5.28; // kg/m³
        double refArea = Math.PI* Math.pow(lander.getRADIUS(), 2);

        Vector3dInterface landerVel = lander.getState().getVelocity();

        Vector3dInterface relativeLateralWindVel = landerVel.sub(windVelocity);

        double lateralDragX = dragCoefficient * airDensity * Math.pow(relativeLateralWindVel.getX(), 2) * refArea * 0.5;
        double lateralDragY = dragCoefficient * airDensity * Math.pow(relativeLateralWindVel.getY(), 2) * refArea * 0.5;

        Vector3dInterface fallWindVel = new Vector(0, -landerVel.getY(), 0);

        double fallDragX = dragCoefficient * airDensity * Math.pow(fallWindVel.getX(), 2) * refArea * 0.5;
        double fallDragY = dragCoefficient * airDensity * Math.pow(fallWindVel.getY(), 2) * refArea * 0.5;

        return new Vector(lateralDragX + fallDragX, lateralDragY + fallDragY, 0);
    }

    public static Vector3dInterface calculateMainThrusterForce(Lander lander, double mainThrustForce){
        return new Vector()
    }


}
