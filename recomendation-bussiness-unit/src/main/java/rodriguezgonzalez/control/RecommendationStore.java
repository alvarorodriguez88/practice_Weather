package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface RecommendationStore {
    void saveRecommendations(EventProcessor processor) throws StoreException;
}
