package org.personal.weatherfit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                Given:
                    - Max temp: %f°C
                    - Min temp: %f°C
                    - Weather: %s
                    - Precipitation: %f%%
                
                    Recommend clothing for men and women in the following categories: top, bottom, outerwear, shoes. \s
                    Use one item per category, selected from this list: ankle-boots.svg, blazer.svg, cardigan.svg, coat-woman.svg, high-heel.svg, hoddies.svg, one-piece-shirt.svg, one-piece-string.svg, padding-man.svg, padding-vest.svg, padding-woman.svg, pants-cargo.svg, pants-man.svg, pants-woman.svg, shirt-oxford.svg, shirt-pockets.svg, shirt-polo.svg, shoes-converse.svg, shoes-flat.svg, shorts-pockets.svg, shorts-woman.svg, shorts.svg, skirt-layered.svg, skirt-long.svg, skirt-short.svg, sleeveless.svg, socks.svg, suit-top.svg, t-shirt-man.svg, t-shirt-short.svg, t-shirt-woman.svg, vest.svg, watch-rectangle.svg, zip-up-man.svg, zip-up-woman.svg.
                
                    Output in JSON format like this:
                    {
                      "male": {
                        "top": {
                          "material": "MATERIAL",
                          "color": "COLOR",
                          "koreanName": "한글명",
                          "englishName": "FILENAME"
                        },
                        "bottom": { ... },
                        "outerwear": { ... },
                        "shoes": { ... }
                      },
                      "female": {
                        "top": {
                          "material": "MATERIAL",
                          "color": "COLOR",
                          "koreanName": "한글명",
                          "englishName": "FILENAME"
                        },
                        "bottom": { ... },
                        "outerwear": { ... },
                        "shoes": { ... }
                      }
                    }
                    
                Please ensure that the response is in pure text format without markdown syntax, and the JSON structure should remain valid.
                
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

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(response);

        try {
            JsonNode root = mapper.readTree(response);

            String maleTop = mapper.writeValueAsString(root.path("male").path("top"));
            String maleBottom = mapper.writeValueAsString(root.path("male").path("bottom"));
            String maleOuter = mapper.writeValueAsString(root.path("male").path("outerwear"));
            String maleShoes = mapper.writeValueAsString(root.path("male").path("shoes"));

            String femaleTop = mapper.writeValueAsString(root.path("female").path("top"));
            String femaleBottom = mapper.writeValueAsString(root.path("female").path("bottom"));
            String femaleOuter = mapper.writeValueAsString(root.path("female").path("outerwear"));
            String femaleShoes = mapper.writeValueAsString(root.path("female").path("shoes"));

            outfit.setMaleTop(maleTop);
            outfit.setMaleBottom(maleBottom);
            outfit.setMaleOuter(maleOuter);
            outfit.setMaleShoes(maleShoes);

            outfit.setFemaleTop(femaleTop);
            outfit.setFemaleBottom(femaleBottom);
            outfit.setFemaleOuter(femaleOuter);
            outfit.setFemaleShoes(femaleShoes);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        String[] lines = response.split("\\n");
//
//        for (String line : lines) {
//            line = line.trim();
//            if (!line.contains(":")) continue;
//
//            String[] parts = line.split(":", 2);
//            if (parts.length < 2) continue;
//
//            String key = parts[0].trim();
//            String value = parts[1].trim();
//
//            switch (key) {
//                case "남성상의" -> outfit.setMaleTop(value);
//                case "남성하의" -> outfit.setMaleBottom(value);
//                case "남성아우터" -> outfit.setMaleOuter(value);
//                case "남성신발" -> outfit.setMaleShoes(value);
//
//                case "여성상의" -> outfit.setFemaleTop(value);
//                case "여성하의" -> outfit.setFemaleBottom(value);
//                case "여성아우터" -> outfit.setFemaleOuter(value);
//                case "여성신발" -> outfit.setFemaleShoes(value);
//
////                case "추가설명" -> outfit.setExtra(value);
//            }
//        }

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

//        dto.setExtra(outfit.getExtra());

        return dto;
    }
}
