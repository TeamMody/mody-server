package com.example.mody.domain.bodytype.service;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.entity.MemberBodyType;
import com.example.mody.domain.bodytype.repository.BodyTypeRepository;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BodyTypeService {
    private final BodyTypeRepository bodyTypeRepository;
    private final MemberBodyTypeRepository memberBodyTypeRepository;

    /**
     * 유저의 가장 최근 체형 분석 결과의 체형 타입을 반환하는 메서드
     * @param member 체형 타입을 조회할 유저
     * @return 마지막 체형 분석이 존재하지 않을 경우 empty Optional을 반환함.
     */
    public Optional<BodyType> findLastBodyType(Member member){
        Optional<MemberBodyType> optionalMemberBodyType =  memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member);
        return optionalMemberBodyType.map(MemberBodyType::getBodyType);
    }
}
