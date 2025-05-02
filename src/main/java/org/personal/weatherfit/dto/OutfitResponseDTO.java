package org.personal.weatherfit.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OutfitResponseDTO {

    private GenderOutfitDTO male;
    private GenderOutfitDTO female;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class GenderOutfitDTO {
        private ItemDTO top;
        private ItemDTO bottom;
        private ItemDTO outerwear;
        private ItemDTO shoes;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ItemDTO {
        private String material;
        private String color;
        private String koreanName;
        private String englishName;
    }

//    private String extra;
}
