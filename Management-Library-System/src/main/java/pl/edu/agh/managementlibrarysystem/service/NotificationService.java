package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.Notification;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Type;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.NotificationRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final OneWayMapper<Notification, NotificationDTO> notificationMapper;

    //returns a string that answers whether a notification
    public String makeNewNotification(String isbn, String email, Date sendingDate, Type type, boolean accepted) {
        String msg = "";
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        Optional<User> user = userRepository.findByEmail(email);
        if (book.isPresent() && user.isPresent()) {
            Optional<IssuedBook> issuedBook = issuedBooksRepository.findIssuedBookByUserIdAndBookIsbn(user.get().getId(), book.get().getIsbn());
            if (issuedBook.isPresent() && issuedBook.get().getBook().equals(book.get())) {
                Notification notification = new Notification();
                notification.setUser(user.get());
                notification.setBooks(book.get());
                notification.setSendingDate(sendingDate);
                notification.setType(type);
                notification.setAccepted(accepted);
                notificationRepository.save(notification);
                msg = "Notification added successfully!";
            }
            if (msg.isEmpty()) {
                msg = "Couldn't make notification because the user provided has no such books issued";
            }
        } else if (book.isPresent()) {
            msg = "Couldn't make notification because no such user exists";
        } else {
            msg = "Couldn't make notification because no such book exists";
        }
        return msg;
    }

    public String resolveNotifications(NotificationDTO notificationDTO) {
        String msg = "";
        String bookISBN = notificationDTO.getBookISBN();
        Long userId = notificationDTO.getUserID();
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification notification : notifications) {
            if (notification.getUser().getId().equals(userId)) {
                if (notification.getBooks().getIsbn().equals(bookISBN)) {
                    notification.setAccepted(true);
                    notificationRepository.save(notification);
                    msg = "Notification(s) resolved!";
                }
            }
        }
        if (msg.isEmpty()) {
            msg = "No such notifications exit";
        }
        return msg;
    }

    public Integer getAmount(String email) {
        return notificationRepository.sumAllByUserEmail(email);
    }

    public List<NotificationDTO> getNotificationsOfUserByEmail(String email) {
        return notificationRepository.findALLByUserEmail(email)
                .stream()
                .map(this.notificationMapper::map)
                .toList();
    }

    public List<NotificationDTO> getNotifications(User user, boolean ignoreResolved) {
        List<Notification> notifications;
        if (user.getPermission().toString().equalsIgnoreCase("normal_user")) {
            notifications = this.notificationRepository.findALLByUserEmail(user.getEmail());
        } else {
            notifications = this.notificationRepository.findAll();
        }
        return notifications
                .stream()
                .filter(notification -> !ignoreResolved || !notification.getAccepted())
                .map(this.notificationMapper::map)
                .toList();
    }

    public String deleteNotification(NotificationDTO notificationDTO) {
        Long id = notificationDTO.getNotificationID();

        if (notificationRepository.findById(id).isPresent()) {
            notificationRepository.deleteById(id);
           return "deleted successfully!";

        }
        return "couldn't delete notification";
    }
}
