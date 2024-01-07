package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class BusinessLeaker implements RecommendationBuilder {
    private EventProcessor processor;


    public BusinessLeaker(EventProcessor processor) {
        this.processor = processor;
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
}