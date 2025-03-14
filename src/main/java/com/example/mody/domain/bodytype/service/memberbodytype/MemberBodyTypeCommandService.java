package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.member.entity.Member;

public interface MemberBodyTypeCommandService {
    BodyTypeAnalysisResponse analyzeBodyType(Member member, String answers);
}
