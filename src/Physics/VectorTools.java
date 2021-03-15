package Physics;

import titan.Vector3dInterface;

public class VectorTools {

    /** Returns the unit vector pointing from the start to the end position
     *
     * @param start start position for the unit vector
     * @param end end position for the unit vector
     * @return unit vector pointing from "start" to "end"
     */
    public static Vector3dInterface directionVector(Vector3dInterface start, Vector3dInterface end ) {
        Vector3dInterface difference = end.sub(start);
        return difference.mul(1/ difference.norm());
    }


}
