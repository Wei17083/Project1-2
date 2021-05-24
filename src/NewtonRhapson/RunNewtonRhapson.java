package NewtonRhapson;

import titan.*;

import java.util.ArrayList;

public class RunNewtonRhapson {

    public static void main(String[] args) {
        Body[] bodies = BodyList.getBodyList();
        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        for (Body b : bodies) {
            positionList.add(b.getPosition());
            velocityList.add(b.getVelocity());
        }

        State initialState = new State(0, positionList, velocityList);

        double earthRadius = 6.371e6;

        Vector3dInterface initialRelativeVelocity = new Vector(31146.60907, -43027.5195177548, -678.364169826266);
        Vector3dInterface initialRelativePosition = VectorTools.getUnitVector(initialRelativeVelocity).mul(earthRadius);

        Vector3dInterface initialVelocity = bodies[3].getVelocity().add(initialRelativeVelocity);
        Vector3dInterface initialPosition = bodies[3].getPosition().add(initialRelativePosition);

        initialState = initialState.setVelocityByID(11, initialVelocity);
        initialState = initialState.setPositionByID(11, initialPosition);

        System.out.println("initial velocity should be: " + initialState.getVelocityList().get(11));

        NewtonRhapson newtonRhapson = new NewtonRhapson(initialState, 365*24*60*60, 500);

        double day = 24*60*60;
        double year = 365.25*day;
        newtonRhapson.findInitialVelocity(initialState, year, day);
    }
}
