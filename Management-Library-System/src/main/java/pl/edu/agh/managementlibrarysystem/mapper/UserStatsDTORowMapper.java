package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.jdbc.core.RowMapper;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStatsDTORowMapper implements RowMapper<UserStatsDTO> {
    @Override
    public UserStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserStatsDTO.builder()
                .id(rs.getInt(1))
                .first(rs.getString(2))
                .last(rs.getString(3))
                .books(rs.getInt(4))
                .reviews(rs.getInt(5))
                .build();
    }
}
