package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Settings;

@Repository
@Transactional
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    String GET_SETTING_VALUE_WITH_GIVEN_NAME = "SELECT s.value from settings s where s.name = :name";

    @Query(value = GET_SETTING_VALUE_WITH_GIVEN_NAME, nativeQuery = true)
    String getSettingValueWithGivenName(@Param("name") String name);
}
