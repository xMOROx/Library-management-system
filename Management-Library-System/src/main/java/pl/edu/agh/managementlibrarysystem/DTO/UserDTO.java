package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String fullname;
    private String email;
    private String permission;
}
