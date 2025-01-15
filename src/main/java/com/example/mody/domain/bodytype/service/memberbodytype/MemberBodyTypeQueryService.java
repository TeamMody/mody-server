package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.member.entity.Member;

public interface MemberBodyTypeQueryService {
    public MemberBodyType findMemberBodyTypeByMember(Member member);

    public BodyTypeAnalysisResponse getBodyTypeAnalysis(String body);
}
