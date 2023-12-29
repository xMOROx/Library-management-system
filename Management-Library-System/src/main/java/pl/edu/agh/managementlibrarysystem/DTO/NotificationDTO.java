package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
@Data
@Builder
public class NotificationDTO {
        private Long notificationID;
        private Long userID;
        private String bookISBN;
        private String bookTitle;
        private String typeOfNotification;
        private Date dateOfAdmition;
        private Boolean status;
}
