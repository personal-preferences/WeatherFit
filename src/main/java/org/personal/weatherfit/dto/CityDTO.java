package org.personal.weatherfit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알려지지 않은 필드는 무시
public class CityDTO {
    private String name;

    @JsonProperty("local_names")
    private Map<String, String> localNames;

    private double lat; // 위도
    private double lon; // 경도

    private String country; // 국가 코드
    private String state; // 주 (미국의 경우)
}
