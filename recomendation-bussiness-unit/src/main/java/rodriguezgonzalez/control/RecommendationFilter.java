package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface RecommendationFilter {
    void filter(String json, String topicName) throws StoreException;
}
