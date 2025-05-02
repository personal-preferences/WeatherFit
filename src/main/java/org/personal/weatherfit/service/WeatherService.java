package org.personal.weatherfit.service;

import lombok.RequiredArgsConstructor;
import org.personal.weatherfit.dto.CityDTO;
import org.personal.weatherfit.dto.GeocodingDTO;
import org.personal.weatherfit.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${OpenWeather.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    // 도시 이름으로 위도와 경도를 가져오기
    public CityDTO getGeocoding(String city) {
        String url = UriComponentsBuilder.fromUriString("http://api.openweathermap.org/geo/1.0/direct")
                .queryParam("q", city)
                .queryParam("limit", 1)
                .queryParam("appid", API_KEY)
                .toUriString();

        try {
            ResponseEntity<List<CityDTO>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CityDTO>>() {}
            );
            return responseEntity.getBody().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 위도와 경도로 도시 이름을 가져오기
    public CityDTO getCity(GeocodingDTO geocodingDTO) {
        String url = UriComponentsBuilder.fromUriString("http://api.openweathermap.org/geo/1.0/reverse")
                .queryParam("lat", geocodingDTO.getLatitude())
                .queryParam("lon", geocodingDTO.getLongitude())
                .queryParam("limit", 1)
                .queryParam("appid", API_KEY)
                .toUriString();

        try {
            ResponseEntity<List<CityDTO>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CityDTO>>() {}
            );
            return responseEntity.getBody().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 도시 이름으로 날씨 정보 가져오기
    @Cacheable(value = "weather", key = "#city + '_' + T(java.time.LocalDateTime).now().getHour()")
    public WeatherDTO getWeatherData(String city) {
        CityDTO cityList = getGeocoding(city);
        String url = UriComponentsBuilder.fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", cityList.getLat())
                .queryParam("lon", cityList.getLon())
                .queryParam("units", "metric")  // 섭씨
                .queryParam("appid", API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, WeatherDTO.class);
    }
}
