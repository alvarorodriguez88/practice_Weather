package rodriguezgonzalez.model;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ProgramController {
    private Timer timer;
    public ProgramController() {
        timer = new Timer();
    }

    public void start(String apiKey, String dataBase) throws IOException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WeatherController controller = new WeatherController();
                try {
                    controller.execute(apiKey, dataBase);
                    System.out.println("Se acaba de hacer jeje...");
                } catch (IOException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        };
        long interval = 1000 * 60 * 10;
        timer.schedule(task, 0, interval);
    }

    public void stop() {
        timer.cancel();
    }
}
