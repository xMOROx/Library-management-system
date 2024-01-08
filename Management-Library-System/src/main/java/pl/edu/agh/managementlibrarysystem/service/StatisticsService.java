package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.ReadBookMapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenresRepository genresRepository;
    private final ReadBookRepository readBookRepository;
    private final ReviewBookRepository reviewBookRepository;
    private final IssuedBooksRepository issuedBookRepository;
    private final ReadBookMapper readBookMapper;
    public List<ReadBookDTO> getBooksReturnedByUser(User u){
        return readBookRepository.findAllByUserId(u.getId())
                .stream()
                .map(readBookMapper::map)
                .toList();
    }
    public List<String> getBasicUserStatistics(User u){
        List<String> statisitics = new ArrayList<>();
        statisitics.add(readBookRepository.getCountOfReadBooks(u.getId()).toString());
        statisitics.add(reviewBookRepository.getCountOfRatingsGiven(u.getId()).toString());
        statisitics.add(reviewBookRepository.getCountOfReviewsGiven(u.getId()).toString());
        statisitics.add(
                Double.valueOf(Double.sum(readBookRepository.getSumOfUserFees(u.getId()),
                        issuedBookRepository.sumAllFeesByUserId(u.getId()))).toString()
        );
        statisitics.add(reviewBookRepository.getAvgOfRatingsGiven(u.getId()).toString());
;

        return statisitics;
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
