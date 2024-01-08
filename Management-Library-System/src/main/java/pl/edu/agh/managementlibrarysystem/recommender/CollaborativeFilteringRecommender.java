package pl.edu.agh.managementlibrarysystem.recommender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.User;

import java.util.List;

@Slf4j
@Component
public class CollaborativeFilteringRecommender {
    private final SimilarityRatingCal userBasedRating;
    private final SimilarityRatingCal itemBasedRating;

    public CollaborativeFilteringRecommender(@Qualifier("userBasedRating") SimilarityRatingCal userBasedRating,
                                             @Qualifier("itemBasedRating") SimilarityRatingCal itemBasedRating) {
        this.userBasedRating = userBasedRating;
        this.itemBasedRating = itemBasedRating;
    }

    public List<RatingLookUp> recommend(User user) {
        log.info("User based rating");
        return userBasedRating.getSimilarRatings(user);
    }


}
