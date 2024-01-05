package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.sql.SQLException;

public class BusinessLeaker implements RecommendationBuilder {
    private EventProcessor processor;
    private SQLiteRecommendationStore storer;

    public BusinessLeaker(EventProcessor processor) throws StoreException {
        this.processor = processor;
        try {
            this.storer = new SQLiteRecommendationStore();
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    @Override
    public void filter(String json, String topicName) throws StoreException {
        if (topicName.equals("prediction.Weather")) {
            try {
                processor.processWeatherEvent(json);
            } catch (StoreException e) {
                throw new StoreException(e.getMessage());
            }
        } else if (topicName.equals("information.Hotel")) {
            try {
                processor.processHotelEvent(json);
            } catch (StoreException e) {
                throw new StoreException(e.getMessage());
            }
        }
    }
    @Override
    public void saveRecommendations(EventProcessor processor) throws StoreException {
        System.out.println("Lista de Ubicaciones: ");
        for (Ubication ubication : processor.getUbications()) {
            System.out.println(ubication);
        }
        System.out.println("Lista de Alojamientos: ");
        for (Lodging lodging : processor.getLodgings()) {
            System.out.println(lodging);
        }
        storer.saveUbications(processor.getUbications());
        storer.saveLodgings(processor.getLodgings());
    }

}