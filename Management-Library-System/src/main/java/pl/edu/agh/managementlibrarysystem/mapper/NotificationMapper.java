package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Notification;
import pl.edu.agh.managementlibrarysystem.model.User;

@Component
@RequiredArgsConstructor
public class NotificationMapper implements OneWayMapper<Notification, NotificationDTO> {

    @Override
    public NotificationDTO map(Notification object) {
        Book book = object.getBooks();
        User user = object.getUser();
        if(book==null){
            return NotificationDTO.builder()
                    .notificationID(object.getNotificationID())
                    .userID(user.getId())
                    .bookISBN(null)
                    .bookTitle(null)
                    .typeOfNotification(object.getType().toString().toLowerCase())
                    .dateOfAdmition(object.getSendingDate())
                    .status(object.getAccepted())
                    .build();
        }
        return NotificationDTO.builder()
                .notificationID(object.getNotificationID())
                .userID(user.getId())
                .bookISBN(book.getIsbn())
                .bookTitle(book.getTitle())
                .typeOfNotification(object.getType().toString().toLowerCase())
                .dateOfAdmition(object.getSendingDate())
                .status(object.getAccepted())
                .build();
    }
}
