package com.example.mody.domain.bodytype.service.memberbodytype;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberBodyTypeCommandServiceImpl implements MemberBodyTypeCommandService {

    private final MemberBodyTypeRepository memberBodyTypeRepository;

    @Override
    public void saveMemberBodyType(MemberBodyType memberBodyType) {
        memberBodyTypeRepository.save(memberBodyType);
    }
}
