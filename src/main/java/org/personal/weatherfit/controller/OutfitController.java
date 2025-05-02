package org.personal.weatherfit.controller;

import lombok.RequiredArgsConstructor;
import org.personal.weatherfit.dto.OutfitResponseDTO;
import org.personal.weatherfit.dto.WeatherDTO;
import org.personal.weatherfit.service.OutfitService;
import org.personal.weatherfit.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OutfitController {

    private final OutfitService outfitService;
    private final WeatherService weatherService;

    @GetMapping({"/", ""})
    public String index(
            @RequestParam(value = "city", required = false, defaultValue = "Seoul") String city,
            Model model) {

        WeatherDTO weather = weatherService.getWeatherData(city);
        OutfitResponseDTO outfit = outfitService.findOutfitByDate(weather);

        model.addAttribute("outfit", outfit);

        return "index";
    }
}
