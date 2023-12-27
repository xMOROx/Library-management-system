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

@Service
@Transactional
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

    public boolean saveBook(BookDTO bookDTO, String authorName, String authorLastname, String publisherName,
                            String genreType) {
        String isbn = bookDTO.getIsbn();
        Book book = this.bookMapper.mapToEntity(bookDTO);

        if (this.bookRepository.findByIsbn(isbn).isPresent()) {
            Alerts.showErrorAlert("Book already exist", "Book with given isbn " + isbn + " already exists");
            return false;
        }

        return this.bookRepository
                .saveNewBookWithGivenParams(book, authorName, authorLastname, publisherName, genreType).isPresent();
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = this.bookRepository.findAll();
        return books
                .stream()
                .map(this.bookMapper::mapToDto)
                .toList();
    }

    public List<BookDTO> getAllAvailableBooks() {
        return this.bookRepository.findAllAvailableBooks()
                .stream()
                .map(this.bookMapper::mapToDto)
                .toList();
    }

    public int getSumOfAllBooks() {
        return this.bookRepository.sumOfAllBooks();
    }

    public int getSumOfAllAvailableBooks() {
        return this.bookRepository.sumOfAllAvailableBooks();
    }

    public int getSumOfAllRemainingBooks() {
        return this.bookRepository.sumOfAllRemainingBooks();
    }

    public int getSumOfAllRemainingAvailableBooks() {
        return this.bookRepository.sumOfAllAvailableRemainingBooks();
    }

    public List<IssuedBookDTO> getIssuedBooks() {
        List<IssuedBook> issuedBooks = this.issuedBooksRepository.findAllUpToDate();
        return issuedBooks
                .stream()
                .map(this.issuedBookMapper::mapToDto)
                .toList();
    }

    public List<IssuedBookDTO> getIssuedBooksByUserId(Long id) {
        List<IssuedBook> issuedBooks = this.issuedBooksRepository.findAllUpToDateByUserId(id);
        return issuedBooks
                .stream()
                .map(this.issuedBookMapper::mapToDto)
                .toList();
    }

    public void updateFee() {
        this.issuedBooksRepository.updateFee();
    }

    public BookDTO findByISBN(String bookISBN) {
        return this.bookRepository.findByIsbn(bookISBN)
                .map(this.bookMapper::mapToDto)
                .orElse(null);
    }

    public boolean checkIfUserHasGivenBook(UserDTO user, BookDTO book) {
        return this.issuedBooksRepository.findIssuedBookByUserIdAndBookIsbn(user.getId(), book.getIsbn()).isPresent();
    }

    public void issueBook(BookDTO book, UserDTO user, Integer days, boolean isTaken) {
        this.issuedBooksRepository.issueBook(book.getIsbn(), user.getId(), days, isTaken);
    }

    public IssuedBookDTO getIssuedBookById(Long bookId, Long userId) {

        return this.issuedBooksRepository.findByBookIdAndUserId(bookId, userId)
                .map(this.issuedBookMapper::mapToDto)
                .orElse(null);
    }

    public boolean renewBook(Long bookId, Long userId, int numberOfDaysToRenew) {
        try {
            this.issuedBooksRepository.renewBook(bookId, userId, numberOfDaysToRenew);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean returnBook(Long bookId, Long userId) {
        return this.issuedBooksRepository.returnBook(bookId, userId);
    }

    public boolean deleteIssuedBook(Long bookId, Long userId) {
        try {
            this.issuedBooksRepository.deleteIssuedBookByBookIdAndUserId(bookId, userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void issueBookToUser(IssuedBookDTO issuedBookDTO) {
        this.issuedBooksRepository.save(this.issuedBookMapper.mapToEntity(issuedBookDTO));

    }

    public void deleteBook(BookDTO bookDTO) {
        this.bookRepository.delete(this.bookMapper.mapToEntity(bookDTO));
    }


    public boolean checkIfBookIsAvailable(BookDTO book) {
        return this.bookRepository.checkIfBookIsAvailable(book.getIsbn()).equalsIgnoreCase("available");
    }
}
