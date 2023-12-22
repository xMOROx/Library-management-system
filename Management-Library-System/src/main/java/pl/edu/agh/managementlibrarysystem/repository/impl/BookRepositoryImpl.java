package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;

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
}
