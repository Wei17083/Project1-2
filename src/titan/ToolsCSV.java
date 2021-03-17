package titan;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
                out.println(s.velocityList.get(i).getZ()+ ", "); ////8th column: Z-Velocity
            }
        }
        out.close();
    }

    //method to return the list of position vectors of a given Body (ID)
    public static List<Vector> getCSVPositions(String s, double ID) throws FileNotFoundException{
        LinkedList<Vector> list = new LinkedList<Vector>();
        FileReader f = new FileReader(s);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader, 1);

        while(Double.parseDouble((Reader.next())) != ID){
            skipLine(Reader, 1);
            skipColumn(Reader,1);
        }

        while(Reader.hasNextLine()){

            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x,y,z));

            skipLine(Reader,3);
            skipColumn(Reader, 2);
        }
        Reader.close();
        return(list);
    }

    //method to return the list of velocity vectors of a given Body (ID)
    public static List<Vector> getCSVVelocities(String s, double ID) throws FileNotFoundException{
        LinkedList<Vector> list = new LinkedList<Vector>();
        FileReader f = new FileReader(s);
        Scanner Reader = new Scanner(f);
        Reader.useDelimiter(",");
        skipLine(Reader, 1);
        skipColumn(Reader,1);

        while(Double.parseDouble((Reader.next())) != ID){
            skipLine(Reader, 1);
            skipColumn(Reader, 1);
        }

        skipColumn(Reader,3);

        while(Reader.hasNextLine()){
            double x = Double.parseDouble(Reader.next());
            double y = Double.parseDouble(Reader.next());
            double z = Double.parseDouble(Reader.next());

            list.add(new Vector(x,y,z));

            skipLine(Reader,3);
            skipColumn(Reader, 5);
        }
        Reader.close();
        return(list);
    }

    //method to let Scanner skip line(s)
    public static void skipLine(Scanner s,int n){
        for(int i = 0; i < n;i++){
            if(s.hasNextLine()){
                s.nextLine();
            }
        }
    }

    //method to let Scanner skip column(s)
    public static void skipColumn(Scanner s,int n){
        for(int i = 0; i < n;i++){
            if(s.hasNext()){
                s.next();
            }
        }
    }

}
