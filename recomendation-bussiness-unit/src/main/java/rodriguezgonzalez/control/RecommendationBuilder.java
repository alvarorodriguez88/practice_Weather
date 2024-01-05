package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface RecommendationBuilder {
    void filter(String json, String topicName) throws StoreException;
    void saveRecommendations(EventProcessor processor) throws StoreException;
}