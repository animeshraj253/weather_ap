package raj.animesh.weatherapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import raj.animesh.weatherapp.R;
import raj.animesh.weatherapp.databinding.ActivityMainBinding;
import raj.animesh.weatherapp.model.WeatherApp;
import raj.animesh.weatherapp.serviceapi.RetrofitInstance;
import raj.animesh.weatherapp.serviceapi.WeatherApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RetrofitInstance retrofitInstance;
    ActivityMainBinding binding;

    private static final String[] permissions = {"android.permission.ACCESS_FINE_LOCATION",
                                "android.permission.ACCESS_COARSE_LOCATION"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        requestPermissions(permissions, 80);


        binding.searchView.clearFocus();

        fetchWeatherDAta("Araria");

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null){
                    fetchWeatherDAta(query);
                    binding.searchView.clearFocus();
                }
                else {
                    Toast.makeText(MainActivity.this, "Enter a valid location name", Toast.LENGTH_SHORT).show();
                }
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    fetchWeatherDAta(newText);
                }
                return true;
            }
        });


    }

    private void fetchWeatherDAta(String Locationsearch) {

        binding.progressBar2.setVisibility(View.VISIBLE);
        String location = Locationsearch.toUpperCase().trim();

        WeatherApiService weatherApiService = RetrofitInstance.getService();

        Call<WeatherApp> call = weatherApiService
                .getWeather(location,
                        "978718b3b2bc4dfe01cb2c44690f08b3",
                        "Metric");
        call.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {
                WeatherApp weatherApp = response.body();
                if (weatherApp != null){

                    Integer temperature = weatherApp.getMain().getTemp().intValue();
                    String temperatureString = temperature.toString()+"°";
                    binding.temperatureTXT.setText(temperatureString);


                    Integer humidityInt = weatherApp.getMain().getHumidity().intValue();
                    String humidityString = humidityInt.toString()+"%";
                    binding.humidityTXT.setText(humidityString);

                    Double windSpeed = weatherApp.getWind().getSpeed().doubleValue();
                    String windSpeedString = windSpeed.toString()+" m/s";
                    binding.windTXT.setText(windSpeedString);

                    Double seaLevel = weatherApp.getMain().getPressure().doubleValue();
                    String seaLevelString = seaLevel.toString()+" hPa";
                    binding.seaLevelTXT.setText(seaLevelString);

                    binding.locationTXT.setText(location);

                    String weatherCondition  = weatherApp.getWeather().get(0).getMain().toString();
                    binding.weatherTypeTXT.setText(weatherCondition);

                    imageChange(weatherCondition);
                    binding.progressBar2.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<WeatherApp> call, Throwable throwable) {
                // code here
            }
        });
    }

    private void imageChange(String w) {
        if (w.equals("Sunny") || w.equals("Clear") || w.equals("Clear Sky")){
            binding.weatherTypeIMG.setImageResource(R.drawable.sunny_weather_image);
        }
        if (w.equals("Partly Clouds") || w.equals("Clouds") || w.equals("Overcast")){
            binding.weatherTypeIMG.setImageResource(R.drawable.cloudy_weather_image);
        }
        if (w.equals("Mist") || w.equals("Foggy")){
            binding.weatherTypeIMG.setImageResource(R.drawable.cloudy_weather_image_2);
        }
        if (w.equals("Light Rain") || w.equals("Drizzle") || w.equals("Moderate Rain") || w.equals("Showers") || w.equals("Rain")){
            binding.weatherTypeIMG.setImageResource(R.drawable.rain_weather_image);
        }
        if (w.equals("Heavy Rain") || w.equals("Thunderstorm") || w.equals("Thunderstorm with Rain") || w.equals("Thunderstorm with Hail")){
            binding.weatherTypeIMG.setImageResource(R.drawable.thumder_weather_image);
        }
        if (w.equals("Light Snow") || w.equals("Blizzard") || w.equals("Moderate Snow") || w.equals("Heavy Snow") || w.equals("Snow")){
            binding.weatherTypeIMG.setImageResource(R.drawable.snow_weather_image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //
            }
            else {
                Toast.makeText(this, "Location permisson is required", Toast.LENGTH_SHORT).show();
            }

        }
    }
}