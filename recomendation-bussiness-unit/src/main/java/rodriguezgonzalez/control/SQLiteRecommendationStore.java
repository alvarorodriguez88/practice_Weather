package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteRecommendationStore implements RecommendationStore {
    private Connection conn;
    private Statement statement;

    public SQLiteRecommendationStore() throws SQLException {
        this.conn = connect("./Datamart.db");
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

    public void updateLodgings(ArrayList<Lodging> lodgings) throws StoreException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE Alojamientos SET PRICE = ?, CHECKOUT = ? WHERE Ubicacion = ? AND Hotel = ? AND WEBSITE = ?"
            );
            for (Lodging lodging : lodgings) {
                preparedStatement.setDouble(1, lodging.getPrice());
                System.out.println(lodging.getPrice() + "€");
                preparedStatement.setString(2, lodging.getCheckOut());
                System.out.println("CheckOut nuevo: " + lodging.getCheckOut());
                preparedStatement.setString(3, lodging.getAcronym());
                System.out.println("Acronym: " + lodging.getAcronym());
                preparedStatement.setString(4, lodging.getHotelName());
                System.out.println("Name: " + lodging.getHotelName());
                preparedStatement.setString(5, lodging.getWebsite());
                System.out.println("Website: " + lodging.getWebsite());
                preparedStatement.addBatch();
                System.out.println("---------------------------------------");
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
    }

    @Override
    public void saveLodgings(ArrayList<Lodging> lodgings) throws StoreException {
        insertLodgings(lodgings);
        updateLodgings(lodgings);
    }

}