package com.example.mody.domain.bodytype.service.bodytype;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.repository.BodyTypeRepository;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BodyTypeQueryServiceImpl implements BodyTypeQueryService {

    private final BodyTypeRepository bodyTypeRepository;

    // 체형 이름으로 BodyType 클래스 조회
    @Override
    public BodyType findByBodyTypeName(String bodyTypeName) {
        Optional<BodyType> optionalBodyType = bodyTypeRepository.findByName(bodyTypeName);
        BodyType bodyType = optionalBodyType.orElseThrow(()-> new BodyTypeException(BodyTypeErrorStatus.BODY_TYPE_NOT_FOUND));
        return bodyType;
    }
}
