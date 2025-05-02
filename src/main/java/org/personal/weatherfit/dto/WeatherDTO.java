package org.personal.weatherfit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알려지지 않은 필드는 무시
public class WeatherDTO {
    private List<WeatherCondition> weather; // 날씨 상태 목록
    private Main main; // 주요 날씨 정보
    private Wind wind; // 바람 정보
    private String name; // 도시 이름

    // --- 내부 클래스 ---
    @Getter @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherCondition {
        private String main; // 날씨 조건 (Clear, Clouds, Rain 등)
        private String description; // 날씨 조건 설명
        private String icon; // 날씨 아이콘 코드
    }

    @Getter @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp; // 온도
        @JsonProperty("feels_like")
        private double feelsLike; // 체감 온도
        @JsonProperty("temp_min")
        private double tempMin; // 최저 기온
        @JsonProperty("temp_max")
        private double tempMax; // 최대 기온
        private int humidity; // 습도
    }

    @Getter @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private double speed; // 풍속
    }
}
