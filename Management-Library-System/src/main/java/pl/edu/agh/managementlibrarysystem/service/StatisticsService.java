package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.repository.AuthorRepository;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.GenresRepository;
import pl.edu.agh.managementlibrarysystem.repository.PublisherRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenresRepository genresRepository;

    public List<Object[]> getPopularBooks(int number){
        return bookRepository.getNumberOfIssues(number);
    }
    public List<Object[]> getPopularAuthors(int number){
        return authorRepository.getNumberOfIssues(number);
    }
    public List<Object[]> getPopularGenres(int number){
        return genresRepository.getNumberOfIssues(number);
    }
}
