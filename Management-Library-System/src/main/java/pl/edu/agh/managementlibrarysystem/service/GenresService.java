package pl.edu.agh.managementlibrarysystem.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.repository.GenresRepository;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenresService {
    private final GenresRepository genreRepository;

    public ObservableList<String> getAllGenres() {
        return FXCollections.observableList(
                this.genreRepository.findAll()
                        .stream()
                        .map(Genre::getGenre)
                        .toList()
        );
    }

    public ObservableList<String> getMainGenres() {
        return FXCollections.observableList(
                this.genreRepository.findAllMainGenres()
                        .stream()
                        .map(Genre::getGenre)
                        .toList()
        );
    }
    public boolean saveGenre(Genre genre) {
        String genreType = genre.getGenre();

        if (this.genreRepository.findByType(genreType).isPresent()) {
            Alerts.showErrorAlert("Genre already exist", "Genre with given type " + genreType + " already exists");
            return false;
        }

        if (genre.getParentGenre() != null) {
            Optional<Genre> parentGenre = this.genreRepository.findByType(genre.getParentGenre().getGenre());
            if(parentGenre.isPresent()) {
                genre =  parentGenre.get().addSubGenre(genre);
                this.genreRepository.save(parentGenre.get());
                return true;
            }

        } else {
            genre.setParentGenre(null);
        }



        this.genreRepository.save(genre);
        return true;

    }

    public Genre getGenreByType(String value) {
        return this.genreRepository.findByType(value).orElse(null);
    }
}
