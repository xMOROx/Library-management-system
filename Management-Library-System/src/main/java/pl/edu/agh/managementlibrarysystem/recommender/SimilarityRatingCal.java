package pl.edu.agh.managementlibrarysystem.recommender;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.ReadBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.util.List;

@Component
public abstract class SimilarityRatingCal {
    protected final ReadBookRepository readBookRepository;
    protected final UserRepository userRepository;

    protected int similarityNum;
    protected int minimalRater;

    protected SimilarityRatingCal(ReadBookRepository readBookRepository, UserRepository userRepository) {
        this.readBookRepository = readBookRepository;
        this.userRepository = userRepository;
    }


    public abstract List<RatingLookUp> getSimilarRatings(User user);
}
