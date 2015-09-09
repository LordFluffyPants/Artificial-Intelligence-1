package Problem1.src.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jakehayhurst on 9/6/15.
 */
public class ImportManager {
    private static ImportManager instance;
    private ArrayList<Integer> startingPoint = new ArrayList<Integer>();
    private int numberOfRows;


    public static ImportManager getInstance()
    {
        if (instance == null)
        {
            instance = new ImportManager();
        }
        return instance;
    }

    private ImportManager()
    {
        importWorldFile();
    }

    private void findNumberOfLines()
    {
        try {
            String path = System.getProperty("user.dir");
            path += "/src/Problem1/src/Data/initial_condition.csv";

            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            boolean startingPointPassed = false;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (startingPointPassed)
                {
                    numberOfRows++;
                }
            }
        }
        catch (FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void importWorldFile(){
        findNumberOfLines();
        try
        {
            String path = System.getProperty("user.dir");
            path += "/src/Problem1/src/Data/initial_condition.csv";

            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            boolean startingPointPassed = false;
            while ((line = bufferedReader.readLine()) != null)
            {
                String[] mappingLine = line.split(",");
                if (startingPointPassed)
                {
                    //TODO implement mapping feature
                }
                else
                {
                    startingPointPassed = true;
                    startingPoint.add(0,Integer.parseInt(mappingLine[0]));
                    startingPoint.add(1,Integer.parseInt(mappingLine[1]));
                }
            }
        }
        catch (FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
