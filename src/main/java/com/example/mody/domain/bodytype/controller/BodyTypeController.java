package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.service.bodytype.BodyTypeQueryService;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeCommandService;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeQueryService;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.service.MemberQueryService;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "체형 분석", description = "체형 분석 API")
@RestController
@RequestMapping("/body-analysis")
@RequiredArgsConstructor
public class BodyTypeController {

    private final ChatGptService chatGptService;
    private final MemberQueryService memberQueryService;
    private final MemberBodyTypeCommandService memberBodyTypeCommandService;
    private final BodyTypeQueryService bodyTypeQueryService;
    private final MemberBodyTypeQueryService memberBodyTypeQueryService;

    @PostMapping("/{memberId}")
    @Operation(summary = "체형 분석 API", description = "OpenAi를 사용해서 사용자의 체형을 분석하는 API입니다. Request Body에는 질문에 맞는 답변 목록을 String으로 보내주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "체형 분석을 진행할 사용자의 member_id 값을 path variable로 보내주세요.", required = true)
    })
    public BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @NotNull @PathVariable Long memberId,
            @RequestBody @NotBlank String answers
    ) {
        // id에 맞는 사용자 조회
        Member member = memberQueryService.findMemberById(memberId);

        // 사용자 정보(닉네임, 성별) 조회
        String nickname = member.getNickname();
        Gender gender = member.getGender();

        // OpenAi API를 통한 체형 분석
        String content = chatGptService.getContent(nickname, gender, answers);
        BodyTypeAnalysisResponse bodyTypeAnalysisResponse = chatGptService.analyzeBodyType(content);

        // MemberBodyType을 DB에 저장
        saveMemberBodyType(content, bodyTypeAnalysisResponse, member);

        return BaseResponse.onSuccess(bodyTypeAnalysisResponse);
    }

    private void saveMemberBodyType(String content, BodyTypeAnalysisResponse bodyTypeAnalysisResponse, Member member) {
        String bodyTypeName = bodyTypeAnalysisResponse.getBodyTypeAnalysis().getType();
        BodyType bodyType = bodyTypeQueryService.findByBodyTypeName(bodyTypeName);
        MemberBodyType memberBodyType = new MemberBodyType(content, member, bodyType);
        memberBodyTypeCommandService.saveMemberBodyType(memberBodyType);
    }

    @GetMapping
    @Operation(summary = "체형 질문 문항 조회 API - 프론트와 협의 필요",
            description = "체형 분석을 하기 위해 질문 문항을 받아 오는 API입니다. 이 부분은 서버에서 바로 프롬프트로 넣는 방법도 있기 때문에 프론트와 협의 후 진행하겠습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<Void> getQuestion() {
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "체형 분석 결과물 조회 API", description = "체형 분석 결과물을 받아 오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "체형 분석 결과를 받아올 사용자의 member_id 값을 path variable로 보내주세요.", required = true)
    })
    public BaseResponse<BodyTypeAnalysisResponse> getBodyType(@NotNull @PathVariable Long memberId) {
        // id에 맞는 사용자 조회
        Member member = memberQueryService.findMemberById(memberId);

        // 사용자에 맞는 체형 분석 내용 조회
        MemberBodyType memberBodyType = memberBodyTypeQueryService.findMemberBodyTypeByMember(member);
        String body = memberBodyType.getBody();

        return BaseResponse.onSuccess(memberBodyTypeQueryService.getBodyTypeAnalysis(body));
    }
}