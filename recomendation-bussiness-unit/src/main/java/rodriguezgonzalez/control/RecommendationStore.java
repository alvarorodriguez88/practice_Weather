package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

public interface RecommendationStore {
    void saveRecommendations() throws StoreException;
    void saveUbicationRecommendation(Ubication ubication) throws StoreException;
    void saveLodgingRecommendation(Lodging lodging) throws StoreException;
}
