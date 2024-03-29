package titan;

import java.awt.*;

/**
 *
 */
public class Body {
    private String name;
    private double mass;
    private final int ID;
    private Vector3dInterface position;
    private Vector3dInterface velocity;
    private final double radius;
    private Color color; // color to visualise


    public final double GRAV_CONSTANT = 6.674E-11;

    /**
     * This method draws the body using its position, radius and color
     */
    public void draw() {
        VectorTools.drawBody(name, position, radius, color);
    }

    /**
     * returns the mass of the object
     * 
     * @return mass of the object
     */
    public double getMass() {
        return mass;
    }

    public int getID() {
        return ID;
    }

    /**
     * returns the velocity of the object
     * 
     * @return velocity vector of the object
     */
    public Vector3dInterface getVelocity() {
        return velocity;
    }

    /**
     * returns the position of the object
     * 
     * @return position vector of the object
     */
    public Vector3dInterface getPosition() {
        return position;
    }

    /**
     * returns the radius of the body
     *
     * @return radius of the body
     */
    public double getRadius() {
        return radius;
    }


    /**
     * Changes the mass of the body
     *
     * @param mass set the mass of the body to this value
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Changes the position vector of the body
     *
     * @param position set the position vector of the body to this value
     */
    public void setPosition(Vector3dInterface position) {
        this.position = position;
    }

    /**
     * changes the velocity vector of the body
     *
     * @param velocity set the velocity
     */
    public void setVelocity(Vector3dInterface velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Constructor of the body class
     *
     * @param name     sets the name of the object
     * @param mass     sets the mass of the object
     * @param position sets the position vector of the object
     * @param velocity sets the velocity vector of the object
     * @param radius   sets the radius of the object
     * @param c        sets the Color of the object, if NULL will be set to black
     */
    public Body(String name, int ID, double mass, Vector3dInterface position, Vector3dInterface velocity, double radius,
            Color c) {
        this.name = name;
        this.ID = ID;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        if (c == null) // if there is no color input, default to black
            color = Color.BLACK;
        else
            color = c;
    }






}
