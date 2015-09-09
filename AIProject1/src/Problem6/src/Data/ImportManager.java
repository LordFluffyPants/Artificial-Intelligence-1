package Problem6.src.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakehayhurst on 9/6/15.
 */
public class ImportManager
{
    private ArrayList<Integer> map = new ArrayList<Integer>();
    private static ImportManager instance;

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
        importInitialState();
    }

    private void importInitialState()
    {
        try
        {
            String path = System.getProperty("user.dir");
            path += "/src/Problem6/src/Data/initial_state.csv";

            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            boolean startingPointPassed = false;
            while ((line = bufferedReader.readLine()) != null)
            {
                String[] mappingLine = line.split(",");
                map.add(Integer.parseInt(mappingLine[0]));
                map.add(Integer.parseInt(mappingLine[1]));
                map.add(Integer.parseInt(mappingLine[2]));
                map.add(Integer.parseInt(mappingLine[3]));
                map.add(Integer.parseInt(mappingLine[4]));
                map.add(Integer.parseInt(mappingLine[5]));
                map.add(Integer.parseInt(mappingLine[6]));
                map.add(Integer.parseInt(mappingLine[7]));
                map.add(Integer.parseInt(mappingLine[8]));
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

    public ArrayList<Integer> getMap()
    {
        return map;
    }

}
