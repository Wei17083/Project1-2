package titan;
import titan.Vector3dInterface;

import java.util.ArrayList;
/** Stores the path of a body as a list of position Vectors
 *
 */
public class Path {
    ArrayList<Vector3dInterface> path;
    ArrayList<Vector3dInterface> velocities;
    double stepSize;

    /** constructor takes in the stepSize(time intervals) and the initial position for the path
     *
     * @param stepSize size of the time intervals corresponding to the positions
     * @param initialPosition starting position vector of the path
     */
    public Path(double stepSize, Vector3dInterface initialPosition, Vector3dInterface initialVelocity) {
        this.stepSize = stepSize;
        path = new ArrayList<Vector3dInterface>();
        path.add(initialPosition);
        velocities = new ArrayList<Vector3dInterface>();
        velocities.add(initialVelocity);
    }

    /** Constructor takes in the stepSize and an ArrayList storing a path of position vectors
     *
     * @param stepSize stepSize size of the time intervals corresponding to the positions
     * @param path ArrayList containing path comprised of position vectors
     */
    public Path(double stepSize, ArrayList<Vector3dInterface> path) {
        this.stepSize = stepSize;
        this.path = path;
    }

    /** adds next position to the path
     *
     * @param position vector to be added to the path
     */
    public void add(Vector3dInterface position, Vector3dInterface velocity) {

        path.add(position);
        velocities.add(velocity);
    }

    /** gets closest approximate position at a specific moment
     *
     * @param t time for which we want to find the position
     */
    public Vector3dInterface getPosition(double t) {
        int index = (int) Math.round(t/stepSize);
        if(index >= path.size()) throw new RuntimeException("path ends before: " + t);
        return path.get(index);
    }


}