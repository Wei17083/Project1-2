package TitanLanding.Controllers;

import TitanLanding.Lander.LanderState;
import titan.Vector3dInterface;

public interface Controller {
    public Vector3dInterface calculateMainThrust(LanderState landerState);
    public double calculateTorque(LanderState landerState);
}
