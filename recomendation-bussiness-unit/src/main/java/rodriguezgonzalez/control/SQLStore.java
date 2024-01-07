package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;
import java.util.ArrayList;

public interface SQLStore {
    void createTables() throws StoreException;
    void saveUbications(ArrayList<Ubication> ubications) throws StoreException;
    void saveLodgings(ArrayList<Lodging> lodgings) throws StoreException;
    void clearTables() throws StoreException;
}