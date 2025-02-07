package com.example.mody.domain.recommendation.service;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.CrawlerErrorStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {

    public String getRandomImageUrl(String keyword) {
        WebDriver driver = threadLocalDriver.get();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            String searchUrl = "https://kr.pinterest.com/search/pins/?q=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            driver.navigate().to(searchUrl);
            log.info("Pinterest 검색 URL 접속 완료: {}", searchUrl);

            // 이미지 태그 검색 결과 로드
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@src, 'https')]")));
            log.info("이미지 태그 검색 결과 로드 완료");

            Set<String> imageUrls = new HashSet<>();
            List<WebElement> images = driver.findElements(By.xpath("//img[contains(@src, 'https')]"));

            int maxImages = Math.min(images.size(), 7); // 최대 이미지 7개(핀터레스트 첫 줄 사진이 7개)
            for (int i = 0; i < maxImages; i++) {
                String url = images.get(i).getAttribute("src");
                if (url != null && !url.isEmpty()) {
                    imageUrls.add(url);
                }
            }

            if (imageUrls.isEmpty()) {
                log.error("이미지 URL을 찾을 수 없음 (키워드: {})", keyword);
                throw new RestApiException(CrawlerErrorStatus.IMAGE_NOT_FOUND);
            }

            List<String> imageList = new ArrayList<>(imageUrls);
            Collections.shuffle(imageList);
            return imageList.get(0);

        } catch (TimeoutException e) {
            log.error("페이지 로드 실패 (키워드: {})", keyword);
            throw new RestApiException(CrawlerErrorStatus.PAGE_LOAD_ERROR);
        } catch (Exception e) {
            log.error("크롤링 실패 (키워드: {}):", keyword);
            throw new RestApiException(CrawlerErrorStatus.CRAWLING_FAILED);
        } finally {
            // WebDriver 종료 대신 ThreadLocal 유지
            driver.manage().deleteAllCookies(); // 세션 정리(메모리 절약)
        }
    }

    // WebDriver 재사용으로 속도 개선
    private static final ThreadLocal<WebDriver> threadLocalDriver = ThreadLocal.withInitial(() -> {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 백그라운드 실행 (UI 렌더링 생략)
        options.addArguments("--disable-gpu"); // GPU 사용 X
        options.addArguments("--window-size=1920,1080"); // 브라우저 창 크기 설정
        options.addArguments("--no-sandbox"); // 샌드박스 모드 비활성화(Docker 환경에서 크롬 드라이버 실행에 필요)
        options.addArguments("--disable-dev-shm-usage"); // /dev/shm 사용 비활성화(Docker 환경에서 크롬 크래시 문제 해결)
//        options.addArguments("--remote-allow-origins=*"); // CORS 대비
//        options.addArguments("--ignore-certificate-errors"); // SSL 차단 대비
        return new ChromeDriver(options);
    });
}