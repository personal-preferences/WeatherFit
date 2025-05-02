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

    private int outfitReqId;
    private String weather;
    private int minTemp;
    private int maxTemp;
    private int rainProb;
//    private String fineDust;

}
