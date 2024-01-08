package pl.edu.agh.managementlibrarysystem.recommender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.ReviewBook;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.ReviewBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("singleton")
public class ItemBasedRating extends SimilarityRatingCal {

    protected ItemBasedRating(ReviewBookRepository readBookRepository,
                              UserRepository userRepository,
                              @Value("5") int similarityNum,
                              @Value("1") int minimalRater) {
        super(readBookRepository, userRepository);
        this.similarityNum = similarityNum;
        this.minimalRater = minimalRater;
    }

    private double calItemSim(Long id1, Long id2) {
        double similarityScore = 0.0;
        double nomBook = 0.0;
        double nomOther = 0.0;
        double userAvg, m1Score, m2Score;
        int minNumCommonUser = 0;
        List<User> userList = userRepository.findAll();

        for (User rater : userList) {
            Long id = rater.getId();

            if (readBookRepository.checkIfUserReviewedBook(id1, rater.getId()) && readBookRepository.checkIfUserReviewedBook(id2, id)) {
                minNumCommonUser++;
                userAvg = readBookRepository.findAllByUserId(id)
                        .stream()
                        .mapToDouble(ReviewBook::getRating)
                        .average()
                        .orElse(0.0);

                ReviewBook readBook1 = readBookRepository.findByUserIdAndBookId(id, id1);
                ReviewBook readBook2 = readBookRepository.findByUserIdAndBookId(id, id2);
                m1Score = readBook1.getRating() - userAvg;
                m2Score = readBook2.getRating() - userAvg;
                similarityScore += m1Score * m2Score;
                nomBook += Math.pow(m1Score, 2);
                nomOther += Math.pow(m2Score, 2);
            }
        }
        if (minNumCommonUser >= minimalRater) {
            return similarityScore / (Math.sqrt(nomBook * nomOther));
        }
        return -100.0;
    }

    private HashMap<Long, List<RatingLookUp>> getSimilarityScores(User user) {
        HashMap<Long, List<RatingLookUp>> allCosineScores = new HashMap<>();
        // book i
        List<ReviewBook> allBooks = readBookRepository.findAll();
        // book j
        List<ReviewBook> allRatedBooks = readBookRepository.findAllRatedBooksByUserId(user.getId());

        for (ReviewBook other : allBooks) {
            Long otherId = other.getBook().getId();
            List<RatingLookUp> newList = new LinkedList<>();

            for (ReviewBook ratedBook : allRatedBooks) {
                Long ratedBookId = ratedBook.getBook().getId();
                if (!otherId.equals(ratedBookId)) {
                    double cosineScore = calItemSim(ratedBookId, otherId);
                    if (cosineScore != -100.0) {
                        newList.add(new RatingLookUp(ratedBookId, cosineScore));
                    }
                }
            }
            if (newList.size() >= similarityNum) {
                allCosineScores.put(otherId, newList);
            }
        }
        return allCosineScores;
    }


    @Override
    public List<RatingLookUp> getSimilarRatings(User user) {
        List<RatingLookUp> calRatings = new LinkedList<>();
        HashMap<Long, List<RatingLookUp>> allScores = getSimilarityScores(user);
        double norm, total, avgR1, avgR2, userRating, cosine;
        int counter;
        long ratedId;

        for (Long book : allScores.keySet()) {

            List<RatingLookUp> allSims = allScores.get(book);
            avgR1 = this.readBookRepository.calculateAvgRatingByBookId(book);
            norm = 0.0;
            total = 0.0;
            counter = 0;

            for (RatingLookUp rate : allSims) {
                ratedId = rate.id();
                avgR2 = this.readBookRepository.calculateAvgRatingByBookId(ratedId);
                userRating = this.readBookRepository.findByUserIdAndBookId(user.getId(), ratedId).getRating();
                if (avgR2 != -1.0) {
                    counter++;
                    cosine = rate.rating();
                    total += cosine * (userRating - avgR2);
                    norm += Math.abs(cosine);
                }
            }
            if (counter >= similarityNum) {
                calRatings.add(new RatingLookUp(book, avgR1 + (total / norm)));
            }
        }
        calRatings.sort(Collections.reverseOrder());

        return calRatings;
    }
}
