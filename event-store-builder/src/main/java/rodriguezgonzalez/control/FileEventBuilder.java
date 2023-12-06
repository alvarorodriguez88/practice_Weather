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
import java.time.ZoneOffset;

public class FileEventBuilder implements EventStoreBuilder {
    private String basePath;

    public FileEventBuilder(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void save(String json) throws StoreException {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            String filePath = makeFilePath(jsonObject);
            writeDirectory(filePath, json);
            System.out.println("Event stored at: " + filePath);
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public void writeDirectory(String filePath, String json) throws IOException {
        File directory = new File(basePath);
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

    public String makeFilePath(JsonObject jsonObject) {
        String tsString = jsonObject.get("ts").getAsString();
        Instant ts = Instant.parse(tsString);
        LocalDate date = ts.atZone(ZoneOffset.UTC).toLocalDate();
        return basePath + date.toString() + ".events";
    }
}
