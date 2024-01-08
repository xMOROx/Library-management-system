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
    private final int MAX_RECOMMENDATIONS = 10;

    public CollaborativeFilteringRecommender(@Qualifier("userBasedRating") SimilarityRatingCal userBasedRating,
                                             @Qualifier("itemBasedRating") SimilarityRatingCal itemBasedRating) {
        this.userBasedRating = userBasedRating;
        this.itemBasedRating = itemBasedRating;
    }

    private void removeDuplicates(List<RatingLookUp> similarRatings) {
        for (int i = 0; i < similarRatings.size(); i++) {
            for (int j = i + 1; j < similarRatings.size(); j++) {
                if (similarRatings.get(i).id().equals(similarRatings.get(j).id())) {
                    similarRatings.remove(j);
                    j--;
                }
            }
        }
    }

    public List<RatingLookUp> recommend(User user) {
        log.info("User based rating");
        List<RatingLookUp> similarRatings = userBasedRating
                .getSimilarRatings(user);
        similarRatings.sort((r1, r2) -> Double.compare(r2.rating(), r1.rating()));

        removeDuplicates(similarRatings);

        return similarRatings.subList(0, MAX_RECOMMENDATIONS);
    }


}
