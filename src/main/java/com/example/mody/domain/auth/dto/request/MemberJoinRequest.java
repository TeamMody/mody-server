package com.example.mody.domain.auth.dto.request;

import com.example.mody.domain.member.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "자체 회원가입 요청 DTO")
@Getter
@NoArgsConstructor
public class MemberJoinRequest {
    @Schema(
            description = "이메일",
            example = "user@example.com"
    )
    @NotNull(message = "이메일은 필수입니다")
    @Email
    private String email;

    @Schema(
            description = "비밀번호",
            example = "user1234!"
    )
    @NotNull(message = "비밀번호는 필수입니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 8자 이상, 영어와 숫자, 그리고 특수문자(@$!%*?&#)를 포함해야 하며, 한글은 사용할 수 없습니다."
    )
    private String password;
}
