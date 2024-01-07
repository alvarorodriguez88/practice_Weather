package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;
import java.util.ArrayList;

public interface SQLStore {
    void createTables() throws StoreException;
    void saveUbications(Ubication ubication) throws StoreException;
    void saveLodgings(Lodging lodging) throws StoreException;
    void clearTables() throws StoreException;
}