package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.Notification;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;
import pl.edu.agh.managementlibrarysystem.model.keys.NotificationKey;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;

@Component
@RequiredArgsConstructor
public class NotificationMapper implements Mapper<Notification, NotificationDTO>{
    private IssuedBooksRepository issuedBooksRepository;
    @Override
    public Notification mapToEntity(NotificationDTO object) {
        return null;
    }

    @Override
    public NotificationDTO mapToDto(Notification object) {
        NotificationKey id = object.getId();
        Book book = object.getBooks();
        User user = object.getUser();
        return NotificationDTO.builder()
                .userID(user.getId())
                .bookISBN(book.getIsbn())
                .bookTitle(book.getTitle())
                .typeOfNotification(object.getType().toString().toLowerCase())
                .dateOfAdmition(object.getSendingDate())
                .status(object.getAccepted())
                .build();
    }
}
