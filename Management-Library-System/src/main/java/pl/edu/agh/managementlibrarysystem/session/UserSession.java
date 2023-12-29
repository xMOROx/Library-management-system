package pl.edu.agh.managementlibrarysystem.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.model.User;

@Component
@Scope("singleton")
public class UserSession {
    @Getter
    @Setter
    private User loggedUser;

    @Getter
    @Setter
    private BookDTO selectedBook;
}
