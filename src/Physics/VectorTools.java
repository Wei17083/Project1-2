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

    public static boolean equals(Vector3dInterface v1, Vector3dInterface v2) {
        return v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
    }

    public static String vectorToString(Vector3dInterface v) {
        return "X: " + v.getX() + " Y: " + v.getY() + " Z: " + v.getZ();
    }

}
