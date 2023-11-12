package rodriguezgonzalez.control;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ProgramController controller = new ProgramController();
        controller.start(args[0], args[1]);
        try {
            Thread.sleep(1000 * 60 * 60 * 48);
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e);
        }
        System.out.println("Program finished");
        controller.stop();
    }
}