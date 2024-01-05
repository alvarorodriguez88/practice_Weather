package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

import java.util.Timer;
import java.util.TimerTask;

public class EventProcessorTimer {

    private final Object lock = new Object();
    private final long MAX_WAIT_TIME = 200000;
    private boolean processingFinished = false;
    private final RecommendationBuilder recommendationBuilder;
    private EventProcessor processor;

    public EventProcessorTimer(RecommendationBuilder recommendationBuilder, EventProcessor processor) {
        this.recommendationBuilder = recommendationBuilder;
        this.processor = processor;
    }

    public void startProcessingWithTimer() throws StoreException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    if (!processingFinished) {
                        processingFinished = true;
                        lock.notify();
                    }
                }
            }
        }, MAX_WAIT_TIME);

        synchronized (lock) {
            while (!processingFinished) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new StoreException(e.getMessage());
                }
            }
        }

        saveRecommendations();
        timer.cancel(); // Cancelar el temporizador
    }

    private void saveRecommendations() throws StoreException {
        // Realizar la acción después de que el procesamiento ha finalizado
        recommendationBuilder.saveRecommendations(processor);
    }
}

