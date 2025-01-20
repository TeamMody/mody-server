package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import com.example.mody.domain.member.entity.Member;

public interface MemberBodyTypeCommandService {
    public BodyTypeAnalysisResponse analyzeBodyType(Member member, String answers);
}
