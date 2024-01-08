package rodriguezgonzalez.view;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    public void connection() {
        String dbPath = "./Datamart.db";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            SwingUtilities.invokeLater(() -> {
                new UserInterfaceBuilder(conn);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
