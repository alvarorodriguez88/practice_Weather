package rodriguezgonzalez.model;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLiteWeatherStore implements WeatherStore {
    public SQLiteWeatherStore() {

    }
    public void initTables(Statement statement, ArrayList<Weather> weathers) throws SQLException {
        //createDB(statement);
        createTable(statement, weathers);
    }

    private static void delete(Statement statement, String tableName, String register) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE register = ?";
        PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
        preparedStatement.setString(1, register);
        preparedStatement.executeUpdate();
    }
    private static void createDB(Statement statement) throws SQLException {
        statement.execute("CREATE DATABASE Tiempo_Canarias;");
    }

    private static void createTable(Statement statement, ArrayList<Weather> weathers) throws SQLException {
        for (Weather weather : weathers){
            statement.execute("CREATE TABLE IF NOT EXISTS " + weather.getLocation().getIsla() + " (" +
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

    private static void dropTable(Statement statement, Weather weather) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS " + weather.getLocation().getIsla() + ";\n");
    }
    private static void dropDataBase(Statement statement, String dbPath) throws SQLException {
        statement.execute("DROP DATABASE " + dbPath + ";");
    }

    public static void insert(Statement statement, ArrayList<Weather> weathers) throws SQLException {
        for (Weather weather : weathers) {
            Instant instant = weather.getTs();
            LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String newDate = dateTime.format(formatter);
            try {
                statement.execute("INSERT INTO " + weather.getLocation().getIsla() + " (DATE, TEMP, POP, HUMIDITY, CLOUDS, WINDSPEED)\n" +
                        "SELECT '" + newDate + "', " + weather.getTemp() + ", " + weather.getPop() + ", " + weather.getHumidity() + ", " + weather.getClouds() + ", " + weather.getWindSpeed() +
                        " WHERE NOT EXISTS (SELECT 1 FROM " + weather.getLocation().getIsla() + " WHERE DATE = '" + newDate + "');");
                System.out.println("Se agregó un nuevo registro en " + newDate);
            } catch (SQLException e){
                System.out.println("ERROR: " + e);
            }
        }
    }
    public static boolean verificate(Statement statement, Weather weather){
        boolean res;
        try{
            if (statement.execute("SELECT * FROM " + weather.getLocation().getIsla() + " WHERE DATE = " + weather.getTs())) res = true;
            else res = false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static void update(Statement statement, ArrayList<Weather> weathers) throws SQLException {
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
            System.out.println("New update in " + newDate);
        }
    }


    public Connection connect(String dbPath) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Conexion");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Override
    public void save(Statement statement, ArrayList<Weather> weathers) throws SQLException {
        update(statement, weathers);
        insert(statement, weathers);
    }
}
