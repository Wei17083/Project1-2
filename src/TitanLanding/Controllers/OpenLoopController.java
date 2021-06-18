package TitanLanding.Controllers;

import TitanLanding.Lander.LanderState;
import TitanLanding.WindSimulation.WindSimulator;
import TitanLanding.WindSimulation.WindValues;
import titan.Vector;
import titan.Vector3dInterface;

public class OpenLoopController implements Controller {

    @Override
    public Vector3dInterface calculateMainThrust(LanderState landerState) {
        Vector3dInterface landerPosition = landerState.getPosition();
        double[] altitudes = WindSimulator.createAltitudes();
        Vector3dInterface[] speeds = WindSimulator.createSpeeds();
        int index = 0;
        for (int i = 0; i < altitudes.length; i++) {
            if(altitudes[i]<landerPosition.getY() && altitudes[i+1]>landerPosition.getY()){
                break;
            }
            index = i;
        }
        return new Vector(-1*speeds[index].getX(),0,0);

    }

    @Override
    public double calculateTorque(LanderState landerState) {
        return 0;
    }
}
