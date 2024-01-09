package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.repository.SettingsRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public void updateFeePerDay(double newFee) {
        this.settingsRepository.updateFeePerDay(newFee);
    }

    public double getFeePerDay() {
        return  Double.parseDouble(this.settingsRepository.getSettingValueWithGivenName("fee_per_day"));
    }
}
