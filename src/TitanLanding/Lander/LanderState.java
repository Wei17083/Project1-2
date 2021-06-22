package TitanLanding.Lander;

import titan.Vector3dInterface;

public class LanderState {
    Vector3dInterface position;
    Vector3dInterface velocity;

    double angle;       //Angle between direction of the thruster and vertical axis
    double angularVelocity;    // positive for clockwise rotation, negative for counter clockwise rotation

    public LanderState (Vector3dInterface position, Vector3dInterface velocity, double angle, double angularVelocity){
        assert(position.getZ() == 0 && velocity.getZ() == 0 );
        this.position = position;
        this.velocity = velocity;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    public Vector3dInterface getPosition() {
        return position;
    }

    public void setPosition(Vector3dInterface position) {
        this.position = position;
    }

    public Vector3dInterface getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3dInterface velocity) {
        this.velocity = velocity;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public String toString(){
        return "Position: " + position.toString() +"\n" +
                "Velocity: " + velocity.toString() + "\n" +
                "Angle: " + angle + "\n" +
                "AngularVelocity: " + angularVelocity + "\n";
    }
}
