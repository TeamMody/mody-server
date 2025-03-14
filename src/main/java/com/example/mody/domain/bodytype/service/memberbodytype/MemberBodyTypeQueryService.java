package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.member.entity.Member;

public interface MemberBodyTypeQueryService {
    BodyTypeAnalysisResponse getBodyTypeAnalysis(Member member);

    MemberBodyType getMemberBodyType(Member member);
}
