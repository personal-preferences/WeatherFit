package org.personal.weatherfit.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OutfitResponseDTO {

    // 날짜별 저장
    private int outfitResId;
    private String outfitDate;

    private String maleTop;
    private String maleBottom;
    private String maleOuter;
    private String maleShoes;

    private String femaleTop;
    private String femaleBottom;
    private String femaleOuter;
    private String femaleShoes;

    private String extra;
    // 이미지 생성
}
