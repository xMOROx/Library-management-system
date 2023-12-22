package pl.edu.agh.managementlibrarysystem.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.model.Publisher;
import pl.edu.agh.managementlibrarysystem.repository.PublisherRepository;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public boolean savePublisher(Publisher publisher) {
        String publisherName = publisher.getName();

        if (this.publisherRepository.findByName(publisherName).isEmpty()) {
            publisherRepository.save(publisher);
            return true;
        }

        Alerts.showErrorAlert("Publisher already exist", "Publisher with given name " + publisherName + " already exists");
        return false;
    }

    public ObservableList<String> getAllPublishers() {
        return FXCollections.observableList(
                this.publisherRepository.findAll()
                        .stream()
                        .map(Publisher::getName)
                        .toList()
        );
    }
}
