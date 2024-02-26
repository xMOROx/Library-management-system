package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.BookStatsDTO;
import pl.edu.agh.managementlibrarysystem.mapper.BookMapper;
import pl.edu.agh.managementlibrarysystem.mapper.BookStatsDTORowMapper;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    private final String BOOK_STATISTICS = "SELECT b.isbn, b.title, p.name, IFNULL(cread.read_count,0), IFNULL(crev.review_count,0) " +
            "FROM books b INNER JOIN publishers p ON p.id = b.publisher_id LEFT JOIN (SELECT b1.id, COUNT(rb.id) AS read_count " +
            "FROM books b1 INNER JOIN read_books rb ON b1.id = rb.book_id GROUP BY b1.id ) cread ON cread.id = b.id LEFT JOIN (SELECT b2.id, COUNT(*) AS review_count " +
            "FROM books b2 INNER JOIN review_books rb ON b2.id = rb.book_id GROUP BY b2.id) crev ON crev.id = cread.id ";

    private final String AUTHOR_BY_NAME_AND_LASTNAME = "SELECT a FROM authors a WHERE a.firstname = ?1 AND a.lastname = ?2";
    private final String PUBLISHER_BY_NAME = "SELECT p FROM publishers p WHERE p.name = ?1";
    private final String GENRE_BY_TYPE = "SELECT g FROM genres g WHERE g.genre = ?1";


    @Transactional
    @SuppressWarnings("unused")
    public Optional<Book> saveNewBookWithGivenParams(Book book, String authorName, String authorLastname, String publisherName, String genreType) {
        return handleBookException(authorName, authorLastname, publisherName, genreType, book);
    }

    @Transactional
    @SuppressWarnings("unused")
    public void deleteBookByIsbn(String isbn) {
        this.entityManager.createQuery("DELETE FROM books b WHERE b.isbn = ?1")
                .setParameter(1, isbn)
                .executeUpdate();
    }

    @Transactional
    @SuppressWarnings("unused")
    public Optional<Book> updateBookWithGivenParams(Long bookId, BookDTO bookDTO, String authorName, String authorLastname, String publisherName, String genreType) {
        Book fromDTO = new BookMapper().mapToEntity(bookDTO);
        Book book = entityManager.find(Book.class, bookId);
        copyNonNullFields(fromDTO, book);

        return handleBookException(authorName, authorLastname, publisherName, genreType, book);
    }

    private Optional<Book> getBook(String authorName, String authorLastname, String publisherName, String genreType, Book book) {
        Genre genre = null;
        Author author = null;
        Publisher publisher = null;

        author = this.getAuthorByNameAndLastname(authorName, authorLastname);

        publisher = this.getPublisherByName(publisherName);

        genre = this.getGenreByType(genreType);

        if (author != null) {
            book.getAuthors().add(author);
            author.getBooks().add(book);
        }

        if (publisher != null) {
            book.setPublisher(publisher);
            publisher.getBooks().add(book);
        }

        if (genre != null) {
            book.getGenres().add(genre);
            genre.getBooks().add(book);
        }

        return Optional.of(this.entityManager.merge(book));
    }

    private Optional<Book> handleBookException(String authorName, String authorLastname, String publisherName, String genreType, Book book) {
        Optional<Book> result = Optional.empty();
        try {
            result = getBook(authorName, authorLastname, publisherName, genreType, book);
        } catch (NoResultException exception) {
            log.error(exception.getMessage());
        }
        return result;
    }

    @Transactional
    @SuppressWarnings("unused")
    public List<BookStatsDTO> getALLBookStats() {
        return jdbcTemplate.query(BOOK_STATISTICS, new BookStatsDTORowMapper());
    }


    private Author getAuthorByNameAndLastname(String name, String lastname) {
        return this.entityManager.createQuery(AUTHOR_BY_NAME_AND_LASTNAME, Author.class)
                .setParameter(1, name)
                .setParameter(2, lastname)
                .getSingleResult();
    }

    private Publisher getPublisherByName(String name) {
        return this.entityManager.createQuery(PUBLISHER_BY_NAME, Publisher.class)
                .setParameter(1, name)
                .getSingleResult();
    }

    private Genre getGenreByType(String type) {
        return this.entityManager.createQuery(GENRE_BY_TYPE, Genre.class)
                .setParameter(1, type)
                .getSingleResult();
    }

    private void copyNonNullFields(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


}
