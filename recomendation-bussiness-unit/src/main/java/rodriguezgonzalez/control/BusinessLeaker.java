package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;

import javax.jms.JMSException;

public class BusinessLeaker implements RecommendationBuilder{
    private String weatherCondition;
    private int nights;
    public BusinessLeaker(String weatherCondition, String nights) {
        this.weatherCondition = weatherCondition;
        this.nights = Integer.parseInt(nights);
    }

    @Override
    public void filter(String json, String topicName) {
        if (topicName.equals("prediction.Weather")){
            System.out.println("Acabo de hacer el filtrado mira el evento: " + json);
            processWeatherEvent(json);
        }
    }
    public void processWeatherEvent(String json){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String stringWeatherCondition = jsonObject.get("weatherCondition").toString();
        System.out.println(stringWeatherCondition);
    }
}
