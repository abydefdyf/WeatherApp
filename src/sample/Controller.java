package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField city;

    @FXML
    private Button getData;

    @FXML
    private Text temp_feels;

    @FXML
    private Text temp_info;

    @FXML
    private Text temp_max;

    @FXML
    private Text temp_min;

    @FXML
    private Text pressure;

    @FXML
    private ImageView image_cloud;

    @FXML
    private ImageView image_sun;

    @FXML
    private ImageView image_brokenClouds;

    @FXML
    private ImageView image_rain;

    @FXML
    void initialize() {
        getData.setOnAction(event -> {
            String getUserCity = city.getText().trim();
            if (!getUserCity.equals("")) {
                String output = getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + getUserCity + "&appid=7f7c4e70785e4fb4fa78786c836b3576&units=metric");


                if (!output.isEmpty()) {
                    JSONObject obj = new JSONObject(output);
                    JSONArray jsonArray = obj.getJSONArray("weather");

                    image_sun.setVisible(false);
                    image_cloud.setVisible(false);
                    image_brokenClouds.setVisible(false);
                    image_rain.setVisible(false);

                    String description = jsonArray.getJSONObject(0).getString("description");

                    image_cloud.setVisible(description.equals("overcast clouds"));

                    if (description.equals("broken clouds") || description.equals("scattered clouds") || description.equals("few clouds")){
                        image_brokenClouds.setVisible(true);
                    }

                    if (description.equals("light rain") || description.equals("moderate rain") || description.equals("heavy intensity rain")){
                        image_rain.setVisible(true);
                    }
                    image_sun.setVisible(description.equals("clear sky"));


                    temp_info.setText("Температура: " + obj.getJSONObject("main").getDouble("temp"));
                    temp_max.setText("Максимум: " + obj.getJSONObject("main").getDouble("temp_max"));
                    temp_min.setText("Минимум: " + obj.getJSONObject("main").getDouble("temp_min"));
                    temp_feels.setText("Ощущается: " + obj.getJSONObject("main").getDouble("feels_like"));
                    pressure.setText("Давление: " + obj.getJSONObject("main").getDouble("pressure"));

                }
            }
        });
    }

    private static String getUrlContent(String urlAdress) {
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAdress);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception ex) {
            System.out.println("Такой город был не найден!");
        }
        return content.toString();
    }
}
