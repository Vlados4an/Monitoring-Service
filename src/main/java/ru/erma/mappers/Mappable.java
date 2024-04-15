package ru.erma.mappers;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(D d);

    List<E> toEntityList(List<D> d);

    D toDto(E e);

    List<D> toDtoList(List<E> e);

}
