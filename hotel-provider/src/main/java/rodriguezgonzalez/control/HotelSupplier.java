package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Hotel;
import rodriguezgonzalez.model.HotelInfo;

import java.util.ArrayList;

public interface HotelSupplier {
    ArrayList<Hotel> getHotel(HotelInfo hotelInfo) throws StoreException;
}
