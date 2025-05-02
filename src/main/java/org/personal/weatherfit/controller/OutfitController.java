package org.personal.weatherfit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.weatherfit.dto.OutfitResponseDTO;
import org.personal.weatherfit.dto.WeatherDTO;
import org.personal.weatherfit.service.OutfitService;
import org.personal.weatherfit.service.WeatherService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OutfitController {

    private final OutfitService outfitService;
    private final WeatherService weatherService;

    private static final String SVG_DIRECTORY = "static/images/svg/"; // Classpath 기준 경로

    @GetMapping({"/", ""})
    public String index(
            @RequestParam(value = "city", required = false, defaultValue = "Seoul") String city,
            Model model) {

        WeatherDTO weather = weatherService.getWeatherData(city);
        OutfitResponseDTO outfit = outfitService.findOutfitByDate(weather);

        model.addAttribute("outfit", outfit);

//        System.out.println(outfit);

        return "index";
    }

    /**
     * 요청된 파일 이름에 해당하는 SVG 파일의 내용을 반환합니다.
     * @param filename URL 경로에서 추출된 SVG 파일 이름 (확장자 포함)
     * @return SVG 파일 내용 또는 오류 응답
     */
    @GetMapping("/svg/{filename:.+}") // 파일 이름에 '.' 포함 가능하도록 .+ 사용
    public ResponseEntity<Resource> getSvgFile(@PathVariable String filename) {

        // 1. 기본적인 파일 이름 유효성 검사 (경로 조작 방지)
        if (filename == null || filename.isBlank() || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            log.warn("Invalid SVG filename requested: {}", filename);
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // 2. Classpath 내에서 리소스 로드 시도
            String resourcePath = SVG_DIRECTORY + filename;
            Resource resource = new ClassPathResource(resourcePath);

            // 3. 리소스 존재 및 접근 가능 여부 확인
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Requested SVG file not found or not readable: {}", resourcePath);
                return ResponseEntity.notFound().build(); // 404 Not Found
            }

            // 4. 응답 헤더 설정 (Content-Type 및 Cache-Control)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("image/svg+xml"));
            // 정적 리소스이므로 브라우저 캐싱 활용 (예: 1시간)
            headers.setCacheControl("public, max-age=3600");

            log.debug("Serving SVG file: {}", resourcePath);
            // 5. 리소스와 헤더, 상태 코드를 포함한 ResponseEntity 반환
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (Exception e) {
            // 예상치 못한 오류 처리
            log.error("Error serving SVG file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

}
