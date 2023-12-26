package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.Mapper;
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
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.sql.Date;
import java.util.HashSet;
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

    @Transactional //returns a string that answers whether a notification was made
    public String makeNewNotification( String isbn, String email, Date sendingDate, Type type, boolean accepted ){
        String msg = "";
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        Optional<User> user = userRepository.findByEmail(email);
        if(book.isPresent() && user.isPresent()){
            List<IssuedBook> issuedBookList = issuedBooksRepository.findByUserId(user.get().getId());
            for (IssuedBook issuedBook : issuedBookList) {
                if (issuedBook.getBook().equals(book.get())) {
                    Notification notification = new Notification(book.get(), user.get(), sendingDate, type, accepted);
                    notificationRepository.save(notification);
                    msg = "Notification added succesfully!";
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
    public Integer getAmount(String email){
        return notificationRepository.sumAllByUserEmail(email);
    }
    public List<Notification> getNotificationsOfUserByEmail(String email){
        return notificationRepository.findALLByUserEmail(email);
    }
    @Transactional
    public List<NotificationDTO> getNotifications(User user) {
        List <Notification> notifications;
        if(user.getPermission().toString().equalsIgnoreCase("normal_user")){
            notifications=this.notificationRepository.findALLByUserEmail(user.getEmail());
        }
        else{
            notifications = this.notificationRepository.findAll();
        }
        return notifications
                .stream()
                .map(this.notificationMapper::mapToDto)
                .toList();
    }
}
