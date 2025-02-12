package com.example.mody.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginReqeust {

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
    private String password;
}
