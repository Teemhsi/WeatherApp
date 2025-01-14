
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WeatherApp {

    private static final String API_KEY = "your_api_key";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String INFLUXDB_URL = "http://localhost:8086";
    private static final String INFLUXDB_DATABASE = "weather_data";
    
    public static void main(String[] args) {
        String[] regions = {"Rome", "Milan", "Florence", "Naples", "Turin"};
        
        InfluxDB influxDB = InfluxDBFactory.connect(INFLUXDB_URL);
        influxDB.setDatabase(INFLUXDB_DATABASE);

        for (String city : regions) {
            try {
                JsonObject weatherData = getWeatherData(city);
                if (weatherData != null) {
                    double temperature = weatherData.getAsJsonObject("main").get("temp").getAsDouble();
                    insertDataIntoInfluxDB(influxDB, city, temperature);
                    System.out.println("Data for " + city + " inserted successfully!");
                } else {
                    System.out.println("No data received for " + city);
                }
                
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Error sleeping between requests: " + e.getMessage());
            }
        }
        
        influxDB.close();
    }

    private static JsonObject getWeatherData(String city) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return JsonParser.parseString(responseBody).getAsJsonObject();
            } else {
                System.err.println("Failed to fetch weather data for " + city);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error making API request: " + e.getMessage());
            return null;
        }
    }

    private static void insertDataIntoInfluxDB(InfluxDB influxDB, String city, double temperature) {
        Point point = Point.measurement("temperature")
                .tag("city", city)
                .addField("temperature", temperature)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        influxDB.write(point);
    }
}
