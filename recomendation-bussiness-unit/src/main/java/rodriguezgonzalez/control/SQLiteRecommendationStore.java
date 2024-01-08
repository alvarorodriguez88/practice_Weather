package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.sql.*;

public class SQLiteRecommendationStore implements SQLStore {
    private final Connection conn;
    private final Statement statement;

    public SQLiteRecommendationStore() throws SQLException {
        this.conn = connect("./Datamart.db");
        this.statement = conn.createStatement();
    }

    private void createLodgingsTable() throws StoreException {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Lodgings (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Ubication TEXT," +
                    "Hotel TEXT," +
                    "WEBSITE TEXT," +
                    "PRICE FLOAT," +
                    "CURRENCY TEXT," +
                    "CHECKIN TEXT," +
                    "CHECKOUT TEXT" +
                    ");");
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createClimateTable() throws StoreException {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Climate (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Ubication TEXT," +
                    "TEMP FLOAT," +
                    "POP FLOAT," +
                    "CLIMATE_CONDITION TEXT" +
                    ");");
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void insertClimate(Ubication ubication) throws StoreException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO Climate (Ubication, TEMP, POP, CLIMATE_CONDITION) " +
                            "VALUES (?, ?, ?, ?)"
            );
            preparedStatement.setString(1, ubication.getAcronym());
            preparedStatement.setDouble(2, ubication.getTemp());
            preparedStatement.setDouble(3, ubication.getPop());
            preparedStatement.setString(4, ubication.getWeatherCondition());
            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackException) {
                throw new StoreException(rollbackException.getMessage());
            }
            throw new StoreException(e.getMessage());
        }
    }

    private void insertLodgings(Lodging lodging) throws StoreException {
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO Lodgings (Ubication, Hotel, WEBSITE, PRICE, CURRENCY, CHECKIN, CHECKOUT) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            )) {
                preparedStatement.setString(1, lodging.getAcronym());
                preparedStatement.setString(2, lodging.getHotelName());
                preparedStatement.setString(3, lodging.getWebsite());
                preparedStatement.setDouble(4, lodging.getPrice());
                preparedStatement.setString(5, lodging.getCurrency());
                preparedStatement.setString(6, lodging.getCheckIn());
                preparedStatement.setString(7, lodging.getCheckOut());
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                throw new StoreException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    public Connection connect(String dbPath) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void clearTables() throws StoreException {
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            statement.execute("DELETE FROM Lodgings;");
            statement.execute("DELETE FROM Climate;");
            statement.execute("DELETE FROM sqlite_sequence WHERE name='Climate';");
            statement.execute("DELETE FROM sqlite_sequence WHERE name='Lodgings';");
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    @Override
    public void createTables() throws StoreException {
        createClimateTable();
        createLodgingsTable();
    }

    @Override
    public void saveUbications(Ubication ubication) throws StoreException {
        insertClimate(ubication);
    }

    @Override
    public void saveLodgings(Lodging lodging) throws StoreException {
        insertLodgings(lodging);
    }

}