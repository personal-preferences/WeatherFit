package org.personal.weatherfit.service;

import lombok.RequiredArgsConstructor;
import org.personal.weatherfit.GeminiClient;
import org.personal.weatherfit.aggregate.Outfit;
import org.personal.weatherfit.dto.OutfitRequestDTO;
import org.personal.weatherfit.dto.OutfitResponseDTO;
import org.personal.weatherfit.dto.WeatherDTO;
import org.personal.weatherfit.repository.OutfitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final GeminiClient geminiClient;
    private final OutfitRepository outfitRepository;

    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // 날짜 받아서 res 조회 -> 있으면 DB에서 조회, 없으면 생성
    public OutfitResponseDTO findOutfitByDate(WeatherDTO weather) {

        OutfitRequestDTO dto = new OutfitRequestDTO(weather.getWeather().get(0).getMain(), weather.getMain().getTempMin(), weather.getMain().getTempMax(), 20);
        Optional<Outfit> optionalOutfit = outfitRepository.findByOutfitDate(today);
        Outfit outfit = optionalOutfit.orElseGet(()->createOutfit(dto));

        return convertToDTO(outfit);
    }

    // 프롬프트 생성
    public String createPrompt(OutfitRequestDTO dto) {

        return """
            아래 정보를 참고하여 남성과 여성 각각에게 어울리는 옷차림을 추천해주세요.

            - 최고 기온: %f도
            - 최저 기온: %f도
            - 날씨: %s
            - 강수 확률: %f%%

            남성과 여성 모두 다음 4가지 항목으로 추천해주세요: 상의, 하의, 아우터, 신발
            각 항목은 다음 형식으로 출력해주세요: 간단한 설명(구체적인 아이템 예시)
            (ex: 가을용 얇은 외투(가디건), 활동성 좋은 바지(면바지))
            
            이외의 마크다운 기호는 넣지 말고, 아래와 같은 포맷으로만 응답해주세요.
            
            남성상의: [내용]
            남성하의: [내용]
            남성아우터: [내용]
            남성신발: [내용]
            여성상의: [내용]
            여성하의: [내용]
            여성아우터: [내용]
            여성신발: [내용]
            추가설명: [내용]
            
            
            """.formatted(
                dto.getMaxTemp(),
                dto.getMinTemp(),
                dto.getWeather(),
                dto.getRainProb()
        );
    }

    // res 생성
    public Outfit createOutfit(OutfitRequestDTO dto) {

        Outfit outfit = new Outfit();

        // 오늘 날짜로 생성
        outfit.setOutfitDate(today);

        String prompt = createPrompt(dto);
        String response = geminiClient.callGemini(prompt);

//        System.out.println(response);

        String[] lines = response.split("\\n");

        for (String line : lines) {
            line = line.trim();
            if (!line.contains(":")) continue;

            String[] parts = line.split(":", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "남성상의" -> outfit.setMaleTop(value);
                case "남성하의" -> outfit.setMaleBottom(value);
                case "남성아우터" -> outfit.setMaleOuter(value);
                case "남성신발" -> outfit.setMaleShoes(value);

                case "여성상의" -> outfit.setFemaleTop(value);
                case "여성하의" -> outfit.setFemaleBottom(value);
                case "여성아우터" -> outfit.setFemaleOuter(value);
                case "여성신발" -> outfit.setFemaleShoes(value);

                case "추가설명" -> outfit.setExtra(value);
            }
        }

        outfitRepository.save(outfit);

        return outfit;
    }

    public OutfitResponseDTO convertToDTO(Outfit outfit) {

        OutfitResponseDTO dto = new OutfitResponseDTO();

        dto.setOutfitResId(outfit.getId());
        dto.setOutfitDate(outfit.getOutfitDate());

        dto.setMaleTop(outfit.getMaleTop());
        dto.setMaleBottom(outfit.getMaleBottom());
        dto.setMaleOuter(outfit.getMaleOuter());
        dto.setMaleShoes(outfit.getMaleShoes());

        dto.setFemaleTop(outfit.getFemaleTop());
        dto.setFemaleBottom(outfit.getFemaleBottom());
        dto.setFemaleOuter(outfit.getFemaleOuter());
        dto.setFemaleShoes(outfit.getFemaleShoes());

        dto.setExtra(outfit.getExtra());

        return dto;
    }
}
