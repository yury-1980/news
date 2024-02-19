package ru.clevertec.service;

import java.util.List;

public interface Service<U, E> {

    U findById(Long id);

    List<U> findByAll(int pageNumber, int pageSize);

    U updatePatch(E e, Long id);

    void delete(Long id);
}
