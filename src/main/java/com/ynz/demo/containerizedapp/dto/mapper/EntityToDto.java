package com.ynz.demo.containerizedapp.dto.mapper;

public interface EntityToDto<E, D> {
    D mapToDto(E e);

    E mapToEntity(D d);
}
