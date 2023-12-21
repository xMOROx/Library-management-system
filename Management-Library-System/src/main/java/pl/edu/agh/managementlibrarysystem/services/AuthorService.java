package pl.edu.agh.managementlibrarysystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.repositories.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private  final AuthorRepository authorRepository;
}
