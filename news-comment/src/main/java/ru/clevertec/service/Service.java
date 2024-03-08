package ru.clevertec.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface Service<U, E> {

    U findById(Long id);

    List<U> findByAll(int pageNumber, int pageSize);

    U updatePatch(E e, Long id, UserDetails userDetails);

    void delete(Long id, UserDetails userDetails);

    List<U> findByAllTextsByPhrase(String predicate, int pageNumber, int pageSize);

}
