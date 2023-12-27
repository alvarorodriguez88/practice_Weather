package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Hotel;
import rodriguezgonzalez.model.HotelInfo;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HotelController {
    private String path;
    private XoteloHotelSupplier xoteloHotelSupplier;
    private ArrayList<HotelInfo> hotelsInfo;
    private JMSHotelStore jmsHotelStore;

    public HotelController(){
        this.path = "./hotels.txt";
        this.xoteloHotelSupplier = new XoteloHotelSupplier();
        this.jmsHotelStore = new JMSHotelStore();
        this.hotelsInfo = new ArrayList<>();
    }

    public void readHotelInfo() throws StoreException {
        try {
            File archivo = new File(path);
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] valores = line.split(":");

                if (valores.length == 3) {
                    HotelInfo hotelInfo = new HotelInfo(valores[0], valores[1], valores[2]);
                    hotelsInfo.add(hotelInfo);
                } else {
                    System.out.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            throw new StoreException(e.getMessage());
        }
    }
    public void execute() throws StoreException{
        try {
            readHotelInfo();
            jmsHotelStore.connect();
            for (HotelInfo hotelInfo : hotelsInfo){
                ArrayList<Hotel> hotels = xoteloHotelSupplier.getHotel(hotelInfo);
                jmsHotelStore.save(hotels);
            }
            jmsHotelStore.getConnection().close();
        } catch (JMSException e){
            throw new StoreException(e.getMessage());
        }
    }
}
