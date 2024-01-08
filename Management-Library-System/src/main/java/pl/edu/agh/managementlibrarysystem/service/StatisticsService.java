package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.ReturnedBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.ReturnedBookMapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.*;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenresRepository genresRepository;
    private final ReturnedBookRepository returnedBookRepository;
    private final ReturnedBookMapper returnedBookMapper;
    public List<ReturnedBookDTO> getBooksReturnedByUser(User u){
        return returnedBookRepository.findAllByUserId(u.getId())
                .stream()
                .map(returnedBookMapper::map)
                .toList();
    }
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
