package com.example.mody.domain.auth.dto.response;

public record TokenResponse (
    String accessToken,
    String refreshToken
) {
}
