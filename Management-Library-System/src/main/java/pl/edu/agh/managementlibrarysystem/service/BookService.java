package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.Mapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.repository.*;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBooksRepository readBooksRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publishersRepository;
    private final GenresRepository genresRepository;
    private final Mapper<Book, BookDTO> bookMapper;
    private final Mapper<IssuedBook, IssuedBookDTO> issuedBookMapper;

    @Transactional
    public boolean saveBook(BookDTO bookDTO, String authorName, String authorLastname, String publisherName, String genreType) {
        String isbn = bookDTO.getIsbn();
        Book book = this.bookMapper.mapToEntity(bookDTO);


        if (this.bookRepository.findByIsbn(isbn).isPresent()) {
            Alerts.showErrorAlert("Book already exist", "Book with given isbn " + isbn + " already exists");
            return false;
        }

        return this.bookRepository.saveNewBookWithGivenParams(book, authorName, authorLastname, publisherName, genreType).isPresent();
    }

    @Transactional
    public List<BookDTO> getBooks() {
        List<Book> books = this.bookRepository.findAll();
        return books
                .stream()
                .map(this.bookMapper::mapToDto)
                .toList();
    }

    @Transactional
    public Integer getSumOfAllBooks() {
        return this.bookRepository.sumOfAllBooks();
    }

    @Transactional
    public Integer getSumOfAllRemainingBooks() {
        return this.bookRepository.sumOfAllRemainingBooks();
    }

    @Transactional
    public List<IssuedBookDTO> getIssuedBooks() {
        List<IssuedBook> issuedBooks = this.issuedBooksRepository.findAll();
        return issuedBooks
                .stream()
                .map(this.issuedBookMapper::mapToDto)
                .toList();
    }

    @Transactional
    public void updateFee() {
        this.issuedBooksRepository.updateFee();
    }

    @Transactional
    public BookDTO findByISBN(String bookISBN) {
        return this.bookRepository.findByIsbn(bookISBN)
                .map(this.bookMapper::mapToDto)
                .orElse(null);
    }

    @Transactional
    public boolean checkIfUserHasBook(UserDTO user) {
        return this.issuedBooksRepository.findByUserId(user.getId())
                .stream()
                .anyMatch(Objects::nonNull);
    }

    @Transactional
    public void issueBook(BookDTO book, UserDTO user, Integer days) {
        this.issuedBooksRepository.issueBook(book.getIsbn(), user.getId(), days);
    }
}
