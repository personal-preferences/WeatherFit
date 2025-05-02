package org.personal.weatherfit;

import lombok.RequiredArgsConstructor;
import org.personal.weatherfit.dto.CityDTO;
import org.personal.weatherfit.dto.GeocodingDTO;
import org.personal.weatherfit.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/city")
    public ResponseEntity<CityDTO> getCity(@ModelAttribute GeocodingDTO geocodingDTO) {
        return ResponseEntity.ok(weatherService.getCity(geocodingDTO));
    }
}
