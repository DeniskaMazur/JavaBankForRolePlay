import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Configurator {

    private static final String configFile = "config";

    public static String config(String key){

        String answer = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(configFile)));
            String line;
            while ((line = reader.readLine()) != null){
                if (line.split("=")[0].equals(key)){
                    answer = line.split("=")[1];
                }
            }
        }catch (IOException e){e.printStackTrace();}
        Log.sPrint("Key: " + key + " configed with value: " + answer);
        return answer;

    }

}
