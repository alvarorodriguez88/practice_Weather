package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Hotel;

import java.util.ArrayList;

public interface HotelStore {
    void save(ArrayList<Hotel> hotel) throws StoreException;
}
