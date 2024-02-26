package pl.edu.agh.managementlibrarysystem.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;
import pl.edu.agh.managementlibrarysystem.mapper.UserStatsDTORowMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;
    String USER_STATISTICS = "SELECT u.id, u.first_name, u.last_name, IFNULL(rea.readb,0) as read_book,IFNULL(revs.revb,0) as reviewed_book " +
            "FROM users u LEFT JOIN ( SELECT u.id, COUNT(*) AS revb " +
            "FROM users u INNER JOIN review_books rb on rb.user_id = u.id GROUP BY u.id) revs ON revs.id = u.id LEFT JOIN (SELECT u.id, COUNT(*) AS readb " +
            "FROM users u INNER JOIN read_books rd on rd.user_id = u.id GROUP BY u.id) rea ON rea.id = u.id WHERE u.permission = \"NORMAL_USER\"";

    public List<UserStatsDTO> getALLUserStats() {
        return jdbcTemplate.query(USER_STATISTICS, new UserStatsDTORowMapper());
    }
}
