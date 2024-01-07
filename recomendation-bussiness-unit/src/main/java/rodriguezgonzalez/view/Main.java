package rodriguezgonzalez.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        String dbPath = "./Datamart.db";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            SwingUtilities.invokeLater(() -> {
                new RecommendationInterface(conn);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}