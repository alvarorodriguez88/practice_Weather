package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface Suscriber {
    void start(RecommendationFilter recommendationBuilder, EventProcessor processor) throws StoreException;
}
