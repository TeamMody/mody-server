package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
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
    private final ObjectMapper objectMapper;

    @Override
    public BodyTypeAnalysisResponse getBodyTypeAnalysis(Member member) {
        MemberBodyType memberBodyType = getMemberBodyType(member);

        // MemberBodyType의 body 내용을 BodyTypeAnalysisResponse로 변환
        try {
            return objectMapper.readValue(memberBodyType.getBody(), BodyTypeAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }

    // Member로 MemberBodyType 조회
    @Override
    public MemberBodyType getMemberBodyType(Member member) {
        return memberBodyTypeRepository.findTopByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));
    }

    /**
     * 유저의 가장 최근 체형 분석 결과의 체형 타입을 반환하는 메서드
     * @param member 체형 타입을 조회할 유저
     * @return 마지막 체형 분석이 존재하지 않을 경우 empty Optional을 반환함.
     */
    public Optional<BodyType> findLastBodyType(Member member){
        Optional<MemberBodyType> optionalMemberBodyType =  memberBodyTypeRepository.findTopByMemberOrderByCreatedAtDesc(member);
        return optionalMemberBodyType.map(MemberBodyType::getBodyType);
    }
}
