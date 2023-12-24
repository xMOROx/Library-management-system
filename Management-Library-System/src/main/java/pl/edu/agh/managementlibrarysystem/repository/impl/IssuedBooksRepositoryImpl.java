package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.repository.SettingsRepository;

@Component
@RequiredArgsConstructor
public class IssuedBooksRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;

    private final SettingsRepository settingsRepository;

    @Transactional
    public void updateFee() {
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));
        String query = "UPDATE issued_books ib SET ib.fee = (DATEDIFF(CURRENT_DATE, ib.issued_date) + 1) * " + feePerDay;

        entityManager.createNativeQuery(query).executeUpdate();
    }
}
