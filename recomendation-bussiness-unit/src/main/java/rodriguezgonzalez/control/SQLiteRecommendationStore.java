package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.sql.*;
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

    public void initTables() throws StoreException {
        createTable();
    }

    public void createTable() throws StoreException {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Alojamientos (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Ubicacion TEXT," +
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

    public void insertLodgings(ArrayList<Lodging> lodgings) throws StoreException {
        try {
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO Alojamientos (Ubicacion, Hotel, WEBSITE, PRICE, CURRENCY, CHECKIN, CHECKOUT) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            for (Lodging lodging : lodgings) {
                // Verificar si el alojamiento ya existe en la base de datos
                if (!checkExistingLodging(lodging)) {
                    preparedStatement.setString(1, lodging.getAcronym()); // Ubicacion
                    preparedStatement.setString(2, lodging.getHotelName());
                    preparedStatement.setString(3, lodging.getWebsite());
                    preparedStatement.setDouble(4, lodging.getPrice());
                    preparedStatement.setString(5, lodging.getCurrency());
                    preparedStatement.setString(6, lodging.getCheckIn());
                    preparedStatement.setString(7, lodging.getCheckOut());
                    preparedStatement.addBatch();
                    System.out.println(preparedStatement);
                }
            }

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

    private boolean checkExistingLodging(Lodging lodging) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Alojamientos " +
                "WHERE Hotel = ? AND WEBSITE = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, lodging.getHotelName());
        preparedStatement.setString(2, lodging.getWebsite());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        }

        return false;
    }
    public void insertUbications(ArrayList<Ubication> ubications) throws StoreException {
        try {
            conn.setAutoCommit(false); // Habilitar modo de transacción

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Ubicaciones (Nombre) VALUES (?)"
            );

            for (Ubication ubication : ubications) {
                if (!checkExistingUbication(ubication)) {
                    preparedStatement.setString(1, ubication.getAcronym());
                    preparedStatement.addBatch();
                    System.out.println(preparedStatement);
                }
            }

            preparedStatement.executeBatch(); // Ejecutar las inserciones en lote
            conn.commit(); // Confirmar la transacción
            conn.setAutoCommit(true); // Restaurar modo de auto-commit

        } catch (SQLException e) {
            try {
                conn.rollback(); // Revertir la transacción en caso de error
            } catch (SQLException e1) {
                throw new StoreException(e1.getMessage());
            }
            throw new StoreException(e.getMessage());
        }
    }
    private boolean checkExistingUbication(Ubication ubication) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Ubicaciones WHERE Nombre = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, ubication.getAcronym());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        }

        return false;
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
        initTables();
        insertUbications(ubications);
    }

    @Override
    public void saveLodgings(ArrayList<Lodging> lodgings) throws StoreException {
        //update(lodgings);
        insertLodgings(lodgings);
    }
}
