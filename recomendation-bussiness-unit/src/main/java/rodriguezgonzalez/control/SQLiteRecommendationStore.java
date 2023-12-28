package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteRecommendationStore implements RecommendationStore {
    private final String dbPath;
    private Connection conn;
    private Statement statement;

    public SQLiteRecommendationStore(String dbPath) throws SQLException {
        this.dbPath = dbPath;
        this.conn = connect(dbPath);
        this.statement = conn.createStatement();
    }

    public void initTables(ArrayList<Ubication> ubications) throws StoreException {
        try {
            createTable(ubications);
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createTable(ArrayList<Ubication> ubications) throws SQLException {
        for (Ubication ubication : ubications) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + ubication.getAcronym() + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "HOTEL TEXT,\n" +
                    "WEBSITE TEXT,\n" +
                    "PRICE INTEGER,\n" +
                    "CURRENCY TEXT,\n" +
                    "CHECKIN TEXT,\n" +
                    "CHECKOUT TEXT" +
                    ");");
        }
    }

    public void insert(ArrayList<Lodging> lodgings) throws StoreException {
        for (Lodging lodging : lodgings) {
            try {
                statement.execute("INSERT INTO " + lodging.getAcronym() + " (HOTEL, WEBSITE, PRICE, CURRENCY, CHECKIN, CHECKOUT)\n" +
                        "VALUES ('" + lodging.getHotelName() + "', '" +
                        lodging.getWebsite() + "', " +
                        lodging.getPrice() + ", '" +
                        lodging.getCurrency() + "', '" +
                        lodging.getCheckIn() + "', '" +
                        lodging.getCheckOut() + "')");
            } catch (SQLException e) {
                throw new StoreException(e.getMessage());
            }
        }
    }

    /*private void update(ArrayList<Weather> weathers) throws SQLException {
        for (Weather weather : weathers) {
            Instant instant = weather.getTs();
            LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String newDate = dateTime.format(formatter);
            String query = "UPDATE " + weather.getLocation().getIsla() +
                    " SET TEMP = '" + weather.getTemp() +
                    "', POP = " + weather.getPop() +
                    ", HUMIDITY = " + weather.getHumidity() +
                    ", CLOUDS = " + weather.getClouds() +
                    ", WINDSPEED = " + weather.getWindSpeed() +
                    " WHERE DATE = '" + newDate + "'";
            statement.execute(query);
        }
    }

     */


    public Connection connect(String dbPath) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Override
    public void saveUbications(ArrayList<Ubication> ubications) throws StoreException {
        initTables(ubications);
    }

    @Override
    public void saveLodgings(ArrayList<Lodging> lodgings) throws StoreException {
        //update(lodgings);
        insert(lodgings);
    }
}
