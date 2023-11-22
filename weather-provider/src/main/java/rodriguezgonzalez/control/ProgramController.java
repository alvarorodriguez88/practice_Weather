package rodriguezgonzalez.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ProgramController {
    private final Timer timer;
    public ProgramController() {
        timer = new Timer();
    }

    public void start(String apiKey, String dataBase) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WeatherController controller = null;
                try {
                    controller = new WeatherController(new SQLiteWeatherStore(dataBase));
                } catch (SQLException e) {
                    System.out.println("ERROR: " + e);
                }
                try {
                    controller.execute(apiKey);
                    System.out.println("Execution done...");
                } catch (SQLException | IOException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        };
        long interval = 1000 * 60 * 60 * 6;
        timer.schedule(task, 0, interval);
    }
}
