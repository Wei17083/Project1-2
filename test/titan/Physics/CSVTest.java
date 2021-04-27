package titan.Physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import titan.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVTest {

    public static void main(String[] args) throws FileNotFoundException {
        // state 1

        ArrayList<Vector3dInterface> positionList = new ArrayList<>();
        positionList.add(new Vector(1, 1, 1)); // body 0
        positionList.add(new Vector(2, 2, 2)); // body 1
        positionList.add(new Vector(3, 3, 3)); // body 2

        ArrayList<Vector3dInterface> velocityList = new ArrayList<>();
        velocityList.add(new Vector(1, 2, 3)); // body 0
        velocityList.add(new Vector(4, 5, 6)); // body 1
        velocityList.add(new Vector(7, 8, 9)); // body 2

        State s0 = new State(0, positionList, velocityList);

        // state 2

        ArrayList<Vector3dInterface> positionList1 = new ArrayList<>();
        positionList1.add(new Vector(-1, -1, -1)); // body 0
        positionList1.add(new Vector(-2, -2, -2)); // body 1
        positionList1.add(new Vector(-3, -3, -3)); // body 2

        ArrayList<Vector3dInterface> velocityList1 = new ArrayList<>();
        velocityList1.add(new Vector(-1, -2, -3)); // body 0
        velocityList1.add(new Vector(-4, -5, -6)); // body 1
        velocityList1.add(new Vector(-7, -8, -9)); // body 2

        State s1 = new State(1, positionList1, velocityList1);

        // state 3

        ArrayList<Vector3dInterface> positionList2 = new ArrayList<>();
        positionList2.add(new Vector(3, 3, 3)); // body 0
        positionList2.add(new Vector(0, 0, 0)); // body 1
        positionList2.add(new Vector(-1, -1, -1)); // body 2

        ArrayList<Vector3dInterface> velocityList2 = new ArrayList<>();
        velocityList2.add(new Vector(-3, -3, -3)); // body 0
        velocityList2.add(new Vector(0, 0, 0)); // body 1
        velocityList2.add(new Vector(1, 1, 1)); // body 2

        State s2 = new State(2, positionList2, velocityList2);

        ArrayList<State> listStates = new ArrayList<>();
        listStates.add(s0); // time step 0
        listStates.add(s1); // time step 1
        listStates.add(s2); // time step 2

        ArrayList<StateInterface> systemPositions = new ArrayList<StateInterface>(listStates);

        ToolsCSV test = new ToolsCSV(systemPositions, 3, "testdataJUnit", "testdataJUnitTrajectory");
        test.createCSV();

        testGetCSVPosB0();
        testGetCSVPVelB0();

        testGetCSVPosB1();
        testGetCSVPVelB1();

        testGetCSVPosB2();
        testGetCSVPVelB2();

    }

    // Test body 0 positions
    @Test
    public static void testGetCSVPosB0() throws FileNotFoundException {
        assertEquals("[( 1.0, 1.0, 1.0), ( -1.0, -1.0, -1.0), ( 3.0, 3.0, 3.0)]",
                (ToolsCSV.getCSVPositions(0)).toString());
    }

    // Test body 0 velocities
    @Test
    public static void testGetCSVPVelB0() throws FileNotFoundException {
        assertEquals("[( 1.0, 2.0, 3.0), ( -1.0, -2.0, -3.0), ( -3.0, -3.0, -3.0)]",
                (ToolsCSV.getCSVVelocities(0)).toString());
    }

    // Test body 1 positions
    @Test
    public static void testGetCSVPosB1() throws FileNotFoundException {
        assertEquals("[( 2.0, 2.0, 2.0), ( -2.0, -2.0, -2.0), ( 0.0, 0.0, 0.0)]",
                (ToolsCSV.getCSVPositions(1)).toString());
    }

    // Test body 1 velocities
    @Test
    public static void testGetCSVPVelB1() throws FileNotFoundException {
        assertEquals("[( 4.0, 5.0, 6.0), ( -4.0, -5.0, -6.0), ( 0.0, 0.0, 0.0)]",
                (ToolsCSV.getCSVVelocities(1)).toString());
    }

    // Test body 2 positions
    @Test
    public static void testGetCSVPosB2() throws FileNotFoundException {
        assertEquals("[( 3.0, 3.0, 3.0), ( -3.0, -3.0, -3.0), ( -1.0, -1.0, -1.0)]",
                (ToolsCSV.getCSVPositions(2)).toString());
    }

    // Test body 2 velocities
    @Test
    public static void testGetCSVPVelB2() throws FileNotFoundException {
        assertEquals("[( 7.0, 8.0, 9.0), ( -7.0, -8.0, -9.0), ( 1.0, 1.0, 1.0)]",
                (ToolsCSV.getCSVVelocities(2)).toString());
    }

    // Test getProbeTrajectory()
    @Test
    public static void testGetProbeTrajectory() throws IOException {
        Vector3dInterface[] expected = new Vector3dInterface[2];
        expected[0] = new Vector(1, 2, 3);
        expected[1] = new Vector(4, 5, 6);

        Vector3dInterface[] traj = ToolsCSV.getProbeTrajectory();
        for (int i = 0; i < traj.length; i++) {
            assertEquals(VectorTools.vectorToString(expected[i]), VectorTools.vectorToString(traj[i]));
        }
    }

    // Test getFinalPosition
    @Test
    public static void testGetFinalProbePosition() throws FileNotFoundException {
        int expected = 1;
        assertEquals(expected, ToolsCSV.getFinalProbePosition());
    }

}
