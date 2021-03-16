package Physics;

import titan.Vector3dInterface;

import java.util.ArrayList;

public class VectorTools {

    /** Returns the unit vector pointing from the start to the end position
     *
     * @param start start position for the unit vector
     * @param end end position for the unit vector
     * @return unit vector pointing from "start" to "end"
     */
    public static Vector3dInterface directionVector(Vector3dInterface start, Vector3dInterface end ) {
        Vector3dInterface difference = end.sub(start);
        return VectorTools.getUnitVector(difference);
    }

    /** Compares two vectors and returns true if they are the same, false otherwise
     *
     * @param v1 first vector to compare
     * @param v2 second vector to compare
     * @return true if vectors are the same, false otherwise
     */
    public static boolean equals(Vector3dInterface v1, Vector3dInterface v2) {
        return v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
    }

    /** Returns string with x, y and z values of a given vector
     *
     * @param v vector to turn into a string
     * @return string with x, y and z coordinates of v
     */
    public static String vectorToString(Vector3dInterface v) {
        return "X: " + v.getX() + " Y: " + v.getY() + " Z: " + v.getZ();
    }

    /** returns unit vector with same direction as given vector
     *
     * @param v vector to turn into unit vector
     * @return unit vector with same direction as v
     */
    public static Vector3dInterface getUnitVector(Vector3dInterface v){
        return v.mul(1/v.norm());
    }

    /** Returns sum of all vectors in an ArrayList
     *
     * @param vectorList ArrayList containing vectors we want to sum
     * @return sum of all the vectors
     */
    public static Vector3dInterface sumAll(ArrayList<Vector3dInterface> vectorList) {
        Vector3dInterface sum = new Vector(0,0,0);
        for (Vector3dInterface v: vectorList) {
            sum.add(v);
        }
        return sum;
    }

}
