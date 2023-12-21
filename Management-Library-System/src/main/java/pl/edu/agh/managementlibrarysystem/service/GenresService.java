package pl.edu.agh.managementlibrarysystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.repository.GenresRepository;

@Service
@RequiredArgsConstructor
public class GenresService {
    private final GenresRepository genreRepository;

}
