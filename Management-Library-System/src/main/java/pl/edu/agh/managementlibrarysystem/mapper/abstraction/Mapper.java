package pl.edu.agh.managementlibrarysystem.mapper.abstraction;

import org.springframework.stereotype.Component;
/**
 * Two-way mapper interface
 * @param <T> Entity
 * @param <S> DTO
 */
@Component
public interface Mapper<T, S> {
    T mapToEntity(S object);

    S mapToDto(T object);
}
