package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserStatsDTO {
    Integer id;
    String first;
    String last;
    Integer books;
    Integer reviews;
}
