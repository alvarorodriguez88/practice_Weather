package rodriguezgonzalez.control;

import rodriguezgonzalez.model.Location;
import rodriguezgonzalez.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLiteWeatherStore implements WeatherStore {
    private final String dbPath;
    private Connection conn;
    private Statement statement;
    public SQLiteWeatherStore(String dbPath) throws SQLException {
        this.dbPath = dbPath;
        this.conn = connect(dbPath);
        this.statement = conn.createStatement();
    }
    public void initTables(ArrayList<Location> locations) throws SQLException {
        createTable(locations);
    }

    private void createTable(ArrayList<Location> locations) throws SQLException {
        for (Location location : locations){
            statement.execute("CREATE TABLE IF NOT EXISTS " + location.getIsla() + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "DATE TEXT,\n" +
                    "TEMP FLOAT NOT NULL,\n" +
                    "POP FLOAT NOT NULL,\n" +
                    "HUMIDITY INTEGER NOT NULL,\n" +
                    "CLOUDS INTEGER NOT NULL,\n" +
                    "WINDSPEED FLOAT NOT NULL" +
                    ");");
        }
    }

    public void insert(ArrayList<Weather> weathers) {
        for (Weather weather : weathers) {
            Instant instant = weather.getTs();
            LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String newDate = dateTime.format(formatter);
            try {
                statement.execute("INSERT INTO " + weather.getLocation().getIsla() + " (DATE, TEMP, POP, HUMIDITY, CLOUDS, WINDSPEED)\n" +
                        "SELECT '" + newDate + "', " + weather.getTemp() + ", " + weather.getPop() + ", " + weather.getHumidity() + ", " + weather.getClouds() + ", " + weather.getWindSpeed() +
                        " WHERE NOT EXISTS (SELECT 1 FROM " + weather.getLocation().getIsla() + " WHERE DATE = '" + newDate + "');");
            } catch (SQLException e){
                System.out.println("ERROR: " + e);
            }
        }
    }

    private void update(ArrayList<Weather> weathers) throws SQLException {
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
    public void save(ArrayList<Weather> weathers) throws SQLException {
        update(weathers);
        insert(weathers);
    }
}