package titan;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ToolsCSV {
    public static void createCSV(ArrayList<State> listStates) throws FileNotFoundException {
        File csvFile = new File("data.csv");
        PrintWriter out = new PrintWriter(csvFile);

        out.println("time, ID, X-Position, Y-Position, Z-Position, X-Velocity, Y-Velocity, Z-Velocity");

        for (State s : listStates) {
            for (int i = 0; i < s.positionList.size(); i++) {
                out.print(s.tState + ", "); //1st column: time
                out.print(i + ", ");    //2nd column: ID
                out.print(s.positionList.get(i).getX() + ", "); //3rd column: X-Position
                out.print(s.positionList.get(i).getY() + ", "); //4th column: Y-Position
                out.print(s.positionList.get(i).getZ() + ", "); //5th column: Z-Position
                out.print(s.velocityList.get(i).getX() + ", "); //6th column: X-Velocity
                out.print(s.velocityList.get(i).getY() + ", "); //7th column: Y-Velocity
                out.println(s.velocityList.get(i).getZ()); ////8th column: Z-Velocity
            }
        }
        out.close();
    }
}
