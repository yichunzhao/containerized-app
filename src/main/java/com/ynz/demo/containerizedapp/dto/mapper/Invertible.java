package com.ynz.demo.containerizedapp.dto.mapper;

import com.ynz.demo.containerizedapp.domain.IsDomain;
import com.ynz.demo.containerizedapp.dto.IsDto;

public interface Invertible<E extends IsDomain, D extends IsDto> {
    D mapToDto(E e);

    E mapToEntity(D d);
}
