package rodriguezgonzalez.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SQLQueryManager {
    private Connection conn;
    private ArrayList<String> selectedUbications;

    public SQLQueryManager(Connection conn) throws SQLException {
        this.conn = conn;
        this.selectedUbications = new ArrayList<>();
    }

    public ArrayList<String> getUbicationsByClimate(String climateCondition) {
        try {
            String climateQuery = "SELECT DISTINCT Ubication FROM Climate WHERE CLIMATE_CONDITION = ?";
            PreparedStatement climateStatement = conn.prepareStatement(climateQuery);
            climateStatement.setString(1, climateCondition);
            ResultSet climateResultSet = climateStatement.executeQuery();

            while (climateResultSet.next()) {
                String ubication = climateResultSet.getString("Ubication");
                System.out.println(ubication);
                selectedUbications.add(ubication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedUbications;
    }

    public ArrayList<String> getHotelsInformation(ArrayList<String> ubications, String checkIn, String checkOut) {
        // TODO calcular numero de dias segun el checkin y checkout.
        int days = calculateDays(checkIn, checkOut);
        System.out.println(days);
        ArrayList<String> hotelsInformation = new ArrayList<>();
        try {
            for (String ubication : ubications) {
                String lodgingQuery = "SELECT COUNT(*) AS DAYS_COUNTER, Hotel, WEBSITE, SUM(PRICE) AS TOTAL_PRICE " +
                        "FROM Lodgings WHERE Ubication = ? AND CHECKIN >= ? AND CHECKOUT <= ? GROUP BY Hotel, WEBSITE ORDER BY TOTAL_PRICE ASC";
                PreparedStatement lodgingStatement = conn.prepareStatement(lodgingQuery);
                lodgingStatement.setString(1, ubication);
                lodgingStatement.setString(2, checkIn);
                lodgingStatement.setString(3, checkOut);
                ResultSet lodgingResultSet = lodgingStatement.executeQuery();
                System.out.println(lodgingStatement);

                while (lodgingResultSet.next()) {
                    int daysCounter = Integer.parseInt(lodgingResultSet.getString("DAYS_COUNTER"));
                    System.out.println(daysCounter);
                    if (days == daysCounter) {
                        String hotel = lodgingResultSet.getString("Hotel");
                        System.out.println(hotel);
                        String website = lodgingResultSet.getString("WEBSITE");
                        System.out.println(website);
                        double totalPrice = lodgingResultSet.getDouble("TOTAL_PRICE");
                        System.out.println(totalPrice);
                        String hotelInfo = ubication + " -> " + hotel + " -> " + website + " -> " + totalPrice;
                        hotelsInformation.add(hotelInfo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelsInformation;
    }

    public int calculateDays(String checkIn, String checkOut) {
        LocalDate startDate = LocalDate.parse(checkIn);
        LocalDate endDate = LocalDate.parse(checkOut);

        // Calculando la diferencia de días
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public ArrayList<String> getHotelsByAcrónimos(ArrayList<String> acrónimos) {
        ArrayList<String> selectedHotels = new ArrayList<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT DISTINCT Hotel FROM Lodgings WHERE Ubication IN (");
            for (int i = 0; i < acrónimos.size(); i++) {
                queryBuilder.append("?");
                if (i < acrónimos.size() - 1) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(")");

            PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString());

            for (int i = 0; i < acrónimos.size(); i++) {
                preparedStatement.setString(i + 1, acrónimos.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                selectedHotels.add(resultSet.getString("Hotel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedHotels;
    }
}
