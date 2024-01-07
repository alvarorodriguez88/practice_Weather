package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface Suscriber {
    void start(RecommendationBuilder recommendationBuilder, RecommendationStore store) throws StoreException;
}