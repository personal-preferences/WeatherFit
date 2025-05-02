package org.personal.weatherfit.service;

import org.junit.jupiter.api.Test;
import org.personal.weatherfit.dto.OutfitRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OutfitServiceTest {

    @Autowired
    private OutfitService outfitService;

//    @Test
//    public void testFindOutfitByDate() {
//        OutfitRequestDTO dto = new OutfitRequestDTO();
//
//        dto.setWeather("맑음");
//        dto.setMinTemp(14);
//        dto.setMaxTemp(22);
//        dto.setRainProb(20);
//
//        System.out.println(outfitService.findOutfitByDate(dto));
//    }


    @Test
    public void testCreateOutfit() {

        OutfitRequestDTO dto = new OutfitRequestDTO();

        dto.setWeather("맑음");
        dto.setMinTemp(14);
        dto.setMaxTemp(22);
        dto.setRainProb(20);

        outfitService.createOutfit(dto);
    }
}