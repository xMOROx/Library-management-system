package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.mapper.NotificationMapper;
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
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final NotificationMapper notificationMapper;

    @Transactional //returns a string that answers whether a notification
    public String makeNewNotification( String isbn, String email, Date sendingDate, Type type, boolean accepted ){
        String msg = "";
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        Optional<User> user = userRepository.findByEmail(email);
        if(book.isPresent() && user.isPresent()){
            List<IssuedBook> issuedBookList = issuedBooksRepository.findByUserId(user.get().getId());
            for (IssuedBook issuedBook : issuedBookList) {
                if (issuedBook.getBook().equals(book.get())) {
                    Notification notification = new Notification();
                    notification.setUser(user.get());
                    notification.setBooks(book.get());
                    notification.setSendingDate(sendingDate);
                    notification.setType(type);
                    notification.setAccepted(accepted);
                    notificationRepository.save(notification);
                    msg = "Notification added successfully!";
                    break;
                }
            }
            if (msg.equals("")) {
                msg = "Couldn't make notification because the user provided has no such books issued";
            }
        }
        else if(book.isPresent()){
            msg = "Couldn't make notification because no such user exists";
        }
        else{
            msg = "Couldn't make notification because no such book exists";
        }
        return msg;
    }
    @Transactional
    public String resolveNotifications(NotificationDTO notificationDTO){
        String msg = "";
        String bookISBN = notificationDTO.getBookISBN();
        Long userId = notificationDTO.getUserID();
        List<Notification> notifications = notificationRepository.findAll();
        for(Notification notification : notifications){
            if(notification.getUser().getId().equals(userId)){
                if(notification.getBooks().getIsbn().equals(bookISBN)){
                    notification.setAccepted(true);
                    notificationRepository.save(notification);
                    msg="Notification(s) resolved!";
                }
            }
        }
        if(msg.equals("")){
            msg="No such notifications exit";
        }
        return msg;
    }

    @Transactional
    public Integer getAmount(String email){
        return notificationRepository.sumAllByUserEmail(email);
    }
    public List<NotificationDTO> getNotificationsOfUserByEmail(String email){
        return notificationRepository.findALLByUserEmail(email)
                .stream()
                .map(this.notificationMapper::mapToDto)
                .toList();
    }
    @Transactional
    public List<NotificationDTO> getNotifications(User user, boolean ignoreUnresolved) {
        List <Notification> notifications;
        if(user.getPermission().toString().equalsIgnoreCase("normal_user")){
            notifications=this.notificationRepository.findALLByUserEmail(user.getEmail());
        }
        else{
            notifications = this.notificationRepository.findAll();
        }
        return notifications
                .stream()
                .filter(notification -> ignoreUnresolved ? notification.getAccepted() : true )
                .map(this.notificationMapper::mapToDto)
                .toList();
    }
    @Transactional
    public String deleteNotification(NotificationDTO notificationDTO){
        Long id =notificationDTO.getNotificationID();
        String msg = "";
        if(notificationRepository.findById(id).isPresent()){
            notificationRepository.deleteById(id);
            msg = "deleted successfully!";
        }
        else{
            msg="couldn't delete notification";
        }
        return msg;
    }
}
