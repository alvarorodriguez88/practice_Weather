/*package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    public static void executeThreads(EventProcessor processor, BusinessLeaker leaker, TopicSuscriber subscriber) {
        Object monitor = new Object();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable startTask = () -> {
            try {
                synchronized (monitor) {
                    subscriber.start(leaker, processor);
                    monitor.notify(); // Notifica al otro hilo que ha terminado
                }
            } catch (StoreException e) {
                e.printStackTrace();
            }
        };

        Runnable saveTask = () -> {
            try {
                synchronized (monitor) {
                    monitor.wait(); // Espera a la notificación del otro hilo
                    subscriber.saveRecommendations(processor);
                }
            } catch (InterruptedException | StoreException e) {
                e.printStackTrace();
            }
        };

        executor.submit(startTask);
        executor.submit(saveTask);

        executor.shutdown();
    }
}

 */