package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.bodytype.service.bodytype.BodyTypeQueryService;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberBodyTypeCommandServiceImpl implements MemberBodyTypeCommandService {

    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ChatGptService chatGptService;
    private final BodyTypeQueryService bodyTypeQueryService;

    @Override
    public BodyTypeAnalysisResponse analyzeBodyType(Member member, String answers) {

        // OpenAi API를 통한 체형 분석
        String content = chatGptService.getContent(member.getNickname(), member.getGender(), answers);
        BodyTypeAnalysisResponse bodyTypeAnalysisResponse = chatGptService.analyzeBodyType(content);

        // MemberBodyType을 DB에 저장
        BodyType bodyType = bodyTypeQueryService.findByBodyTypeName(bodyTypeAnalysisResponse.getBodyTypeAnalysis().getType());
        memberBodyTypeRepository.save(new MemberBodyType(content, member, bodyType));

        return bodyTypeAnalysisResponse;
    }
}
