package rodriguezgonzalez.control;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class DatalakeFileHandler {
    private static final String DIRECTORIO_DATALAKE = "./";

    public DatalakeFileHandler() {
    }
    public File findLastWeatherFile() {
        String subdirectorioWeather = "datalake/eventstore/prediction.Weather/weather-provider";
        return findLastFile(subdirectorioWeather);
    }

    public File findLastHotelFile() {
        String subdirectorioHotel = "datalake/eventstore/information.Hotel/hotel-provider";
        return findLastFile(subdirectorioHotel);
    }

    private File findLastFile(String subdirectorio) {
        File dir = new File(DIRECTORIO_DATALAKE + File.separator + subdirectorio);
        File[] archivos = dir.listFiles((dir1, nombreArchivo) -> nombreArchivo.matches("\\d{8}\\.events"));

        if (archivos != null && archivos.length > 0) {
            Arrays.sort(archivos, Comparator.comparing(File::getName).reversed());
            return archivos[0];
        }
        return null;
    }

}
