package Solvers.derivatives;

import titan.Vector3dInterface;
import titan.VectorTools;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DerivativePosition implements Derivative {
    private ArrayList<Vector3dInterface> VELOCITIES;
    private Vector3dInterface VELOCITY;

    public DerivativePosition(ArrayList<Vector3dInterface> velocities) {
        VELOCITIES = velocities;
    }

    public DerivativePosition(Vector3dInterface velocity) {
        VELOCITY = velocity;
    }

    public Vector3dInterface getDerivative(int ID) {
        return VectorTools.clone(VELOCITIES.get(ID));
    }

    public Vector3dInterface getDerivative() {
        return VectorTools.clone(VELOCITIES.get(1));
    }

    public ArrayList<Vector3dInterface> getAllDerivatives() {
        ArrayList<Vector3dInterface> velocities = new ArrayList<>();
        for (Vector3dInterface velocity : VELOCITIES) {
            velocities.add(VectorTools.clone(velocity));
        }
        return velocities;
    }
}
