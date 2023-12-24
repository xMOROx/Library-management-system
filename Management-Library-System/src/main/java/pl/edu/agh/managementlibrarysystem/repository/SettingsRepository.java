package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    @Query(value = "SELECT s.value from settings s where s.name = ?1", nativeQuery = true)
    String getSettingValueWithGivenName(@Param("name") String name);
}
