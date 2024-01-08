package rodriguezgonzalez.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SQLQueryManager {
    private final Connection conn;
    private final ArrayList<String> selectedUbications;

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
                selectedUbications.add(ubication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedUbications;
    }

    public ArrayList<String> getHotelsInformation(ArrayList<String> ubications, String checkIn, String checkOut) {
        int days = calculateDays(checkIn, checkOut);
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
                while (lodgingResultSet.next()) {
                    int daysCounter = Integer.parseInt(lodgingResultSet.getString("DAYS_COUNTER"));
                    if (days == daysCounter) {
                        String hotel = lodgingResultSet.getString("Hotel");
                        String website = lodgingResultSet.getString("WEBSITE");
                        double totalPrice = lodgingResultSet.getDouble("TOTAL_PRICE");
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
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}
