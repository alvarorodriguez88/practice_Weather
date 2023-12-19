package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

import java.util.Timer;
import java.util.TimerTask;

public class ProgramController {
    private final Timer timer;

    public ProgramController() {
        timer = new Timer();
    }

    public void start(String apiKey) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    new WeatherController(apiKey).execute(apiKey);
                    System.out.println("Execution done...");
                } catch (StoreException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        };
        long interval = 1000 * 60 * 60 * 6;
        timer.schedule(task, 0, interval);
    }
}
