package com.example.mody.domain.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.mody.domain.image.dto.response.PresignedUrlResponse;
import com.example.mody.domain.image.dto.response.S3UrlResponse;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.S3ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public List<PresignedUrlResponse> getPostPresignedUrls(Long memberId, List<String> filenames) {
        // 게시물당 하나의 UUID를 생성
        String uuid = UUID.randomUUID().toString();

        List<PresignedUrlResponse> presignedUrls = new ArrayList<>();
        for (String filename : filenames) {
            String key = String.format("deploy/%d/%s/%s", memberId, uuid, filename); // 테스트용 deploy 폴더, 추후 post 폴더로 변경 예정
            Date expiration = getExpiration(); // 유효 기간
            GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrl(key, expiration);

            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            presignedUrls.add(PresignedUrlResponse.of(url.toExternalForm(), key));
        }
        return presignedUrls;
    }

    public PresignedUrlResponse getProfilePresignedUrl(Long memberId, String filename) {
        // key값 설정(profile 경로 + 멤버ID + 랜덤 값 + filename)
        String key = String.format("profile/%d/%s/%s", memberId, UUID.randomUUID(), filename);

        // 유효 기간
        Date expiration = getExpiration();

        // presigned url 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrl(key, expiration);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return PresignedUrlResponse.of(url.toExternalForm(), key);
    }

    // 업로드용(put) presigned url 생성하는 메서드
    private GeneratePresignedUrlRequest generatePresignedUrl(String key, Date expiration) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, key)
                    .withMethod(HttpMethod.PUT)
                    .withKey(key)
                    .withExpiration(expiration);
            generatePresignedUrlRequest.addRequestParameter(
                    Headers.S3_CANNED_ACL,
                    CannedAccessControlList.PublicRead.toString());
            return generatePresignedUrlRequest;
        } catch (AmazonS3Exception e) {
            throw new RestApiException(S3ErrorStatus.BUCKET_NOT_FOUND);
        } catch (Exception e) {
            throw new RestApiException(S3ErrorStatus.PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    // 유효 기간 설정
    private static Date getExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 2 * 1000 * 60 * 60; // 2시간 설정
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    // 프론트에서 전달받은 key를 이용해 S3 URL 생성 및 반환(테스트용)
    public S3UrlResponse getS3Url(String key) {
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        log.info("s3Url: {}", s3Url);
        return S3UrlResponse.from(s3Url);
    }
}