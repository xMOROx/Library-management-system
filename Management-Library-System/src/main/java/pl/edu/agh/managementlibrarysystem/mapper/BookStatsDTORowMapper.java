package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.jdbc.core.RowMapper;
import pl.edu.agh.managementlibrarysystem.DTO.BookStatsDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookStatsDTORowMapper implements RowMapper<BookStatsDTO> {
    @Override
    public BookStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BookStatsDTO.builder()
                .isbn(rs.getString(1))
                .title(rs.getString(2))
                .publisher(rs.getString(3))
                .readTimes(rs.getInt(4))
                .reviews(rs.getInt(5))
                .build();
    }
}
