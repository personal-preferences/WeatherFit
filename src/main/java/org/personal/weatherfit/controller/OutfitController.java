package org.personal.weatherfit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OutfitController {

//    private final OutfitService outfitService;
//    private final WeatherService weatherService;

    @GetMapping({"/", ""})
    public String index(
            @RequestParam(value = "city", required = false, defaultValue = "Seoul") String city,
            Model model) {

        return "index";
    }
}
