package com.example.mody.domain.member.web.dto;

import com.example.mody.domain.member.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

public class MemberRequestDTO {

    @Getter
    public static class JoinDto{
        @Email @NotBlank
        String email;

        @NotBlank
        String password;

        @NotBlank
        String nickname;

        String profileUrl;

        @NotNull
        Date birthDate;

        @NotNull
        Integer gender;

        @NotNull
        Integer height;
    }

    @Getter
    public static class LoginDto{
        @Email @NotBlank
        String email;
        @NotBlank
        String password;
    }
}
