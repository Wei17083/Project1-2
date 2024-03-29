package Solvers.derivatives;

import Solvers.GravitationalForce;
import titan.Body;
import titan.Vector;
import titan.Vector3dInterface;
import titan.VectorTools;

import java.util.ArrayList;

/**
 * class represents derivative of the velocity of a single body
 */
public class DerivativeVelocity implements Derivative {
    private final ArrayList<Vector3dInterface> POSITIONS;
    private final Body[] BODY_LIST;

    public DerivativeVelocity(ArrayList<Vector3dInterface> positions, Body[] bodyList) {
        POSITIONS = positions;
        BODY_LIST = bodyList;
    }

    /** calculates the acceleration at point p in the solar system,
     *  not taking any bodies in account that are situated at point p
     *
     * @param ID     ID of the body on which the acceleration will be calculated
     * @return      acceleration of body with id ID
     */
    public Vector3dInterface getDerivative(int ID){
        Vector3dInterface acceleration = new Vector();
        Vector3dInterface currentPosition = POSITIONS.get(ID);
        for (Body body:BODY_LIST) {
            Vector3dInterface bodyPosition = POSITIONS.get(body.getID());
            if (!(ID == body.getID()))  {
                acceleration = acceleration.add(GravitationalForce.acceleration(currentPosition, POSITIONS.get(body.getID()),body.getMass()));
            }
        }
        return acceleration;
    }

    public ArrayList<Vector3dInterface> getAllDerivatives(){
        ArrayList<Vector3dInterface> accelerations = new ArrayList<>();
        for (Body body:BODY_LIST) {
            accelerations.add(this.getDerivative(body.getID()));
        }
        return accelerations;
    }
}
