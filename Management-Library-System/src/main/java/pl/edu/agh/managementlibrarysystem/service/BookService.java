package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.*;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.*;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReadBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReturnedBookRepository;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBookRepository readBooksRepository;
    private final ReturnedBookRepository returnedBookRepository;
    private final Mapper<Book, BookDTO> bookMapper;
    private final Mapper<IssuedBook, IssuedBookDTO> issuedBookMapper;
    private final OneWayMapper<ReadBook, ReadBookDTO> readBookMapper;
    private final OneWayMapper<Book, BookDetailsDTO> bookDetailsMapper;
    private final OneWayMapper<ReturnedBook, ReadBookAvailableToVoteDTO> returnedBookReadBookAvailableToVoteDTOOneWayMapper;

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

    @Transactional
    public Double getTotalFeesByUserId(Long id) {
        return this.issuedBooksRepository.sumAllFeesByUserId(id);
    }

    public boolean updateBook(BookDTO bookDTO, String authorName, String authorLastname, String publisherName,
                              String genreType) {
        Optional<Book> b = bookRepository.findByIsbn(bookDTO.getIsbn());
        return bookRepository.updateBookWithGivenParams(b.get().getId(), bookDTO, authorName, authorLastname, publisherName, genreType).isPresent();
    }

    public void updateFee() {
        this.issuedBooksRepository.updateFee();
    }

    public BookDTO findByISBN(String bookISBN) {
        return this.bookRepository.findByIsbn(bookISBN)
                .map(this.bookMapper::mapToDto)
                .orElse(null);
    }

    public Optional<Book> findBookByISBN(String bookISBN) {
        return this.bookRepository.findByIsbn(bookISBN);
    }

    public void deleteByISBN(String bookISBN) {
        Optional<Book> toDelete = this.bookRepository.findByIsbn(bookISBN);
        try {
            toDelete.ifPresent(book -> this.bookRepository.deleteById(book.getId()));
        } catch (DataIntegrityViolationException e) {
            Alerts.showErrorAlert("Unable to delete a book.", "Book is used by other users");
        }


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

    public void deleteBookByIsbn(String isbn) {
        this.bookRepository.deleteBookByIsbn(isbn);
    }

    public boolean checkIfBookIsAvailable(BookDTO book) {
        return this.bookRepository.checkIfBookIsAvailable(book.getIsbn()).equalsIgnoreCase("available");
    }

    public BookDetailsDTO getBookDetails(String bookISBN) {
        return this.bookRepository.findByIsbn(bookISBN)
                .map(this.bookDetailsMapper::map)
                .orElse(null);
    }

    public BookDetailsDTO getBookDetails(Long id) {
        return this.bookRepository.findById(id)
                .map(this.bookDetailsMapper::map)
                .orElse(null);
    }

    public List<ReadBookDTO> getAllReadBookForUser(User loggedUser) {
        return this.readBooksRepository.findAllByUserId(loggedUser.getId())
                .stream()
                .map(readBookMapper::map)
                .toList();
    }

    public List<ReadBookAvailableToVoteDTO> getAllReadBookAvailableToVoteByUserId(User loggedUser) {
        return this.returnedBookRepository.findAllReadBookAvailableToVoteByUserId(loggedUser.getId())
                .stream()
                .map(this.returnedBookReadBookAvailableToVoteDTOOneWayMapper::map)
                .toList();
    }


    public boolean reviewBook(long bookId, long userId, String review, double rating) {
        return this.readBooksRepository.reviewBook(bookId, userId, review, rating);
    }

    public boolean checkIfBookIsIssuedByUser(Long bookId, Long userId) {
        return this.issuedBooksRepository.findByBookIdAndUserId(bookId, userId).isPresent();
    }
}
