package Physics;
import titan.Vector3dInterface;

import java.util.ArrayList;
/** Stores the path of a body as a list of position Vectors
 *
 */
public class Path {
    ArrayList<Vector3dInterface> path;
    double stepSize;

    /** constructor takes in the stepSize(time intervals) and the initial position for the path
     *
     * @param stepSize size of the time intervals corresponding to the positions
     * @param initialPosition starting position vector of the path
     */
    public Path(double stepSize, Vector3dInterface initialPosition) {
        this.stepSize = stepSize;
        path = new ArrayList<Vector3dInterface>();
        path.add(initialPosition);
    }

    /** adds next position to the path
     *
     * @param position vector to be added to the path
     */
    public void add(Vector3dInterface position) {
        path.add(position);
    }

    /** gets closest approximate position at a specific moment
     *
     * @param t time for which we want to find the position
     */
    public Vector3dInterface getPosition(double t) {
        int index = (int) (t/stepSize);
        if(index >= path.size()) throw new RuntimeException("path ends before: " + t);
        return path.get(index);
    }


}
