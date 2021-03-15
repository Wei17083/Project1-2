package Physics;
import  titan.*;

/**
 *
 */
public class  Body {
    double mass;
    Vector position;
    Vector velocity;
    double radius;

    /** returns the mass of the object
     * @return mass of the object
     */
    public double getMass() {
        return mass;
    }

    /** returns the velocity of the object
     * @return velocity vector of the object
     */
    public Vector getVelocity() {
        return velocity;
    }

    /** returns the position of the object
     * @return position vector of the object
     */
    public Vector getPosition() {
        return position;
    }

    /** returns the radius of the body
     *
     * @return radius of the body
     */
    public double getRadius() {
        return radius;
    }

    /** Constructor of the body class
     *
     * @param mass sets the mass of the object
     * @param position sets the position vector of the object
     * @param velocity sets the velocity vector of the object
     */
    public Body(double mass, Vector position, Vector velocity, double radius){
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
    }

    /** Changes the mass of the body
     * @param mass set the mass of the body to this value
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /** Changes the position vector of the body
     * @param position set the position vector of the body to this value
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /** changes the velocity vector of the body
     * @param velocity set the velocity
     */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }
}


