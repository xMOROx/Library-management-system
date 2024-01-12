package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookStatsDTO {
    String isbn;
    String title;
    String publisher;
    Integer readTimes;
    Integer reviews;
}
