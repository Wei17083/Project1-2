package NewtonRhapson;

import titan.*;

public class NewtonRhapson {

    private boolean firstMission = true;
    private Vector3dInterface initialVelocity;
    private Vector3dInterface finalPosition;

    public Vector3dInterface findInitialVelocity(State initialState, Vector3dInterface finalPosition, double stepSize, double finalTime) {

        if (!closeEnough()){ //get next
            this.initialVelocity = getNextAttempt();
            finalPosition = getMinDistance();
        }
    }

    private Vector3dInterface calculateDistanceFromDestination(){ // basically g(v)

        int destinationPlanetID;
        if(firstMission)
            destinationPlanetID = 8; // titan ID
        else
            destinationPlanetID = 3; // earth ID

        Vector3dInterface destinationPlanet = BodyList.getBodyList()[destinationPlanetID].getPosition();

        double x = destinationPlanet.getX() - finalPosition.getX();
        double y = destinationPlanet.getY() - finalPosition.getY();
        double z = destinationPlanet.getZ() - finalPosition.getZ();

        return new Vector(x, y, z);

    }

    private boolean closeEnough() {

        int destinationPlanetID;
        if(firstMission)
            destinationPlanetID = 8; // titan ID
        else
            destinationPlanetID = 3; // earth ID

        Vector3dInterface destinationPlanetPosition = BodyList.getBodyList()[destinationPlanetID].getPosition();

        double x1 = finalPosition.getX();
        double y1 = finalPosition.getY();
        double z1 = finalPosition.getZ();

        double x2 = destinationPlanetPosition.getX();
        double y2 = destinationPlanetPosition.getY();
        double z2 = destinationPlanetPosition.getZ();

        double squaredDistance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2);
        double distance = Math.sqrt(squaredDistance);

        double destinationPlanetRadius = BodyList.getBodyList()[destinationPlanetID].getRadius();

        if((destinationPlanetRadius+100000) < distance && distance < (destinationPlanetRadius+300000))
            return true;
        return false;
    }

    private Vector3dInterface getNextAttempt(){

        double oldX = this.initialVelocity.getX();
        double oldY = this.initialVelocity.getY();
        double oldZ = this.initialVelocity.getZ();

        double newX, newY, newZ;

        Vector3dInterface distanceFromDestination = calculateDistanceFromDestination(); // g(vk)

        Matrix jacobianMatrix = getJacobianMatrix();

        Vector3dInterface vectorToSub = jacobianMatrix.inverse().mul(distanceFromDestination);

        newX = oldX - vectorToSub.getX();
        newY = oldY - vectorToSub.getY();
        newZ = oldZ - vectorToSub.getZ();

        return new Vector(newX, newY, newZ);
    }

    private Vector3dInterface getMinDistance(){
        // returns position where probe gets given velocity
    }

    private Matrix calculateJacobianMatrix(){
        // returns the jacobian derivative matrix  given Vk
    }

    public void startComingBack(){ this.firstMission = false; }

}
