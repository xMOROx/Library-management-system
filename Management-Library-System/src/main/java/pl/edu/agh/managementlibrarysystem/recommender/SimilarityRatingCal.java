package pl.edu.agh.managementlibrarysystem.recommender;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.ReviewBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.util.List;

@Component
public abstract class SimilarityRatingCal {
    protected final ReviewBookRepository readBookRepository;
    protected final UserRepository userRepository;

    protected int similarityNum;
    protected int minimalRater;

    protected SimilarityRatingCal(ReviewBookRepository readBookRepository, UserRepository userRepository) {
        this.readBookRepository = readBookRepository;
        this.userRepository = userRepository;
    }


    public abstract List<RatingLookUp> getSimilarRatings(User user);
}
