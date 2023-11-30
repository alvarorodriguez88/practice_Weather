package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class StoreEvents {
    public StoreEvents() {

    }
    public void store(String json) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String basePath = makeBasePath(jsonObject);
        String filePath = makeFilePath(jsonObject, basePath);
        writeDirectory(basePath, filePath, json);
        System.out.println("Event stored at: " + filePath);
    }
    public void writeDirectory(String basePath, String filePath, String json) throws IOException {
        File directory = new File(basePath);
        File file = new File(filePath);
        if (!directory.exists()){
            directory.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e){
                System.out.println("ERROR: " + e);
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.write(json + "\n");
        writer.close();
    }
    public String makeBasePath(JsonObject jsonObject){
        String ss = jsonObject.get("ss").getAsString();
        return  "eventstore/prediction.Weather/" + ss + "/";
    }
    public String makeFilePath(JsonObject jsonObject, String basePath){
        String tsString = jsonObject.get("ts").getAsString();
        Instant ts = Instant.parse(tsString);
        LocalDate date = ts.atZone(ZoneOffset.UTC).toLocalDate();
        return basePath  + date.toString() + ".events";
    }
}
