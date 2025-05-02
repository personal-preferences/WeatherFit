package org.personal.weatherfit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OutfitRequestDTO {

    private String weather;
    private double minTemp;
    private double maxTemp;
    private double rainProb;
//    private String fineDust;

}
