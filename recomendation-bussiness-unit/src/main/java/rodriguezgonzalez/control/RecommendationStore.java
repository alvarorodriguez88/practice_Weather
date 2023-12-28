package rodriguezgonzalez.control;

import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;
import java.util.ArrayList;

public interface RecommendationStore {
    void saveUbications(ArrayList<Ubication> ubications) throws StoreException;
    void saveLodgings(ArrayList<Lodging> lodgings) throws StoreException;
}
