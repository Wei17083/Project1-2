package TitanLanding.Controllers;

import TitanLanding.Lander.Lander;
import TitanLanding.Lander.LanderState;
import titan.Vector3dInterface;

public interface Controller {
    public double calculateMainThrust(LanderState landerState);
    public double calculateTorque(LanderState landerState);
}
