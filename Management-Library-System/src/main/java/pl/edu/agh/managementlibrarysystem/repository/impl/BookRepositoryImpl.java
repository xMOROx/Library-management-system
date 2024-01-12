package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.BookStatsDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;
import pl.edu.agh.managementlibrarysystem.mapper.BookMapper;
import pl.edu.agh.managementlibrarysystem.mapper.BookStatsDTORowMapper;
import pl.edu.agh.managementlibrarysystem.mapper.UserStatsDTORowMapper;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    @Transactional
    @SuppressWarnings("unused")
    public Optional<Book> saveNewBookWithGivenParams(Book book, String authorName, String authorLastname, String publisherName, String genreType) {
        Genre genre = null;
        Author author = null;
        Publisher publisher = null;

        try {
            author = this.entityManager.createQuery("SELECT a FROM authors a WHERE a.firstname = ?1 AND a.lastname = ?2", Author.class)
                    .setParameter(1, authorName)
                    .setParameter(2, authorLastname)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

        try {
            publisher = this.entityManager.createQuery("SELECT p FROM publishers p WHERE p.name = ?1", Publisher.class)
                    .setParameter(1, publisherName)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

        try {
            genre = this.entityManager.createQuery("SELECT g FROM genres g WHERE g.genre = ?1", Genre.class)
                    .setParameter(1, genreType)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

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


    public void deleteBookByIsbn(String isbn) {
        this.entityManager.createQuery("DELETE FROM books b WHERE b.isbn = ?1")
                .setParameter(1, isbn)
                .executeUpdate();
    }

    public Optional<Book> updateBookWithGivenParams(Long bookId, BookDTO bookDTO, String authorName, String authorLastname, String publisherName, String genreType) {
        Genre genre = null;
        Author author = null;
        Publisher publisher = null;

        BookMapper m = new BookMapper();
        Book fromDTO = m.mapToEntity(bookDTO);
        Book book = entityManager.find(Book.class,bookId);
        copyNonNullFields(fromDTO,book);
        try {
            author = this.entityManager.createQuery("SELECT a FROM authors a WHERE a.firstname = ?1 AND a.lastname = ?2", Author.class)
                    .setParameter(1, authorName)
                    .setParameter(2, authorLastname)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

        try {
            publisher = this.entityManager.createQuery("SELECT p FROM publishers p WHERE p.name = ?1", Publisher.class)
                    .setParameter(1, publisherName)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

        try {
            genre = this.entityManager.createQuery("SELECT g FROM genres g WHERE g.genre = ?1", Genre.class)
                    .setParameter(1, genreType)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

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

    String BOOK_STATISTICS = "SELECT b.isbn, b.title, p.name, IFNULL(cread.read_count,0), IFNULL(crev.review_count,0) FROM books b INNER JOIN publishers p ON p.id = b.publisher_id LEFT JOIN (SELECT b1.id, COUNT(rb.id) AS read_count FROM books b1 INNER JOIN read_books rb ON b1.id = rb.book_id GROUP BY b1.id ) cread ON cread.id = b.id LEFT JOIN (SELECT b2.id, COUNT(*) AS review_count FROM books b2 INNER JOIN review_books rb ON b2.id = rb.book_id GROUP BY b2.id) crev ON crev.id = cread.id ";
    public List<BookStatsDTO> getALLBookStats() {
        RowMapper<BookStatsDTO> mapper = new BookStatsDTORowMapper();
        return jdbcTemplate.query(BOOK_STATISTICS, mapper);
    }
}
