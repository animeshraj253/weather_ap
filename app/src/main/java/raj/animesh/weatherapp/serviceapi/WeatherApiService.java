package raj.animesh.weatherapp.serviceapi;

import raj.animesh.weatherapp.model.WeatherApp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<WeatherApp> getWeather(
            @Query("q")String city,
            @Query("appid")String appid,
            @Query("units")String units
    );
}
