package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.stereotype.Component;
/**
 * @param <T> Entity
 * @param <V> DTO
 */
@Component
public interface Mapper<T, V> {
    T mapToEntity(V object);

    V mapToDto(T object);
}
