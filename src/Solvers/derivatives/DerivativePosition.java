package Solvers.derivatives;

import titan.Vector3dInterface;
import titan.VectorTools;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DerivativePosition implements Derivative {
    private final ArrayList<Vector3dInterface> VELOCITIES;
    public DerivativePosition(ArrayList<Vector3dInterface> velocities) {
        VELOCITIES = velocities;
    }

    public Vector3dInterface getDerivative(int ID) {
        return VectorTools.clone(VELOCITIES.get(ID));
    }

    public ArrayList<Vector3dInterface> getAllDerivatives() {
        ArrayList<Vector3dInterface> velocities = new ArrayList<>();
        for (Vector3dInterface velocity : VELOCITIES) {
            velocities.add(VectorTools.clone(velocity));
        }
        return velocities;
    }
}
