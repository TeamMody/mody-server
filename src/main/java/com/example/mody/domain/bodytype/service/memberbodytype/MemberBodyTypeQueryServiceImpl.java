package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberBodyTypeQueryServiceImpl implements MemberBodyTypeQueryService {

    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Member로 MemberBodyType 조회
    @Override
    public MemberBodyType findMemberBodyTypeByMember(Member member) {
        Optional<MemberBodyType> optionalMemberBodyType = memberBodyTypeRepository.findMemberBodyTypeByMember(member);
        return optionalMemberBodyType.orElseThrow(()-> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));
    }

    // MemberBodyType의 body 내용을 BodyTypeAnalysisResponse로 변환
    @Override
    public BodyTypeAnalysisResponse getBodyTypeAnalysis(String body) {
        // String -> DTO 변환
        try {
            return objectMapper.readValue(body, BodyTypeAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }
}
