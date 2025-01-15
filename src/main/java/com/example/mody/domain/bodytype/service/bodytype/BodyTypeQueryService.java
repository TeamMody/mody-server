package com.example.mody.domain.bodytype.service.bodytype;

import com.example.mody.domain.bodytype.entity.BodyType;

public interface BodyTypeQueryService {
    BodyType findByBodyTypeName(String bodyTypeName);
}
