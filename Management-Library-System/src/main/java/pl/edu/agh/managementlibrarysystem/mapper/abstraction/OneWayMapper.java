package pl.edu.agh.managementlibrarysystem.mapper.abstraction;

import org.springframework.stereotype.Component;

/**
 * One way mapper interface
 * @param <T> Object provided to map
 * @param <S> Object returned from map
 */
@Component
public interface OneWayMapper<T, S> {
    /**
     * Map object from T to S
     *
     * @param object T
     * @return S
     */
    S map(T object);
}
