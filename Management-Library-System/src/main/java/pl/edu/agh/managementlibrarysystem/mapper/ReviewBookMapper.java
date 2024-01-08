package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.ReviewBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.ReviewBook;
@Component
public class ReviewBookMapper implements OneWayMapper<ReviewBook, ReviewBookDTO> {
    @Override
    public ReviewBookDTO map(ReviewBook object) {
        Book book = object.getBook();
        String authorsAsString = Book.getAuthorsAsString(book);
        String genresAsString = Book.getGenresAsString(book);

        return ReviewBookDTO
                .builder()
                .review(object.getReview())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authors(authorsAsString == null ? "---" : authorsAsString)
                .genres(genresAsString == null ? "---" : genresAsString)
                .rating(object.getRating())
                .edition(book.getEdition())
                .build();
    }

}
