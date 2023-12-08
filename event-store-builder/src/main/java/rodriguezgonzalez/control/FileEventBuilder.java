package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileEventBuilder implements EventStoreBuilder {
    private String basePath;
    private File directory;
    private Instant ts;
    private LocalDate date;
    private String ss;

    public FileEventBuilder(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void save(String json) throws StoreException {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            formatJson(jsonObject);
            String filePath = makeFilePath();
            writeDirectory(filePath, json);
            System.out.println("Event stored at: " + filePath);
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public void writeDirectory(String filePath, String json) throws IOException {
        directory = new File(basePath + "eventstore/prediction.Weather/" + ss + "/");
        File file = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("ERROR: " + e);
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.write(json + "\n");
        writer.close();
    }

    public String makeFilePath() {
        return directory + "/" + date.toString() + ".events";
    }
    public void formatJson(JsonObject jsonObject){
        String tsString = jsonObject.get("ts").getAsString();
        ts = Instant.parse(tsString);
        date = ts.atZone(ZoneOffset.UTC).toLocalDate();
        ss = jsonObject.get("ss").getAsString();
    }
}
