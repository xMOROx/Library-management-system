package pl.edu.agh.managementlibrarysystem.recommender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.ReadBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("singleton")
public class UserBasedRating extends SimilarityRatingCal {


    protected UserBasedRating(ReadBookRepository readBookRepository,
                              UserRepository userRepository,
                              @Value("50") int similarityNum,
                              @Value("2") int minimalRater) {
        super(readBookRepository, userRepository);
        this.similarityNum = similarityNum;
        this.minimalRater = minimalRater;
    }

    private double getUserAvgRating(User user) {
        return readBookRepository.findAllByUserId(user.getId())
                .stream()
                .mapToDouble(ReadBook::getRating)
                .average()
                .orElse(0.0);
    }

    private double calUserSim(User user, User other) {
        List<ReadBook> userBooks = readBookRepository.findAllByUserId(user.getId());

        double userAvgRating = userBooks
                .stream()
                .mapToDouble(ReadBook::getRating)
                .average()
                .orElse(0.0);


        List<ReadBook> otherBooks = readBookRepository.findAllByUserId(other.getId());

        double otherAvgRating = otherBooks
                .stream()
                .mapToDouble(ReadBook::getRating)
                .average()
                .orElse(0.0);


        double similarityScore = 0.0;
        double nomUser = 0.0;
        double nomOther = 0.0;
        int minNumCommon = 0;
        double userScore, otherScore;

        for (ReadBook book : userBooks) {
            for (ReadBook otherBook : otherBooks) {
                if (book.getBook().getId().equals(otherBook.getBook().getId())) {
                    minNumCommon++;
                    userScore = book.getRating() - userAvgRating;
                    otherScore = otherBook.getRating() - otherAvgRating;
                    similarityScore += (userScore) * (otherScore);
                    nomUser += Math.pow(userScore, 2);
                    nomOther += Math.pow(otherScore, 2);
                }
            }
        }

        if (minNumCommon >= 2 && similarityScore != 0.0 && nomUser != 0.0 && nomOther != 0.0) {
            return similarityScore / (Math.sqrt(nomUser * nomOther));
        }

        return -100.0;
    }

    private List<RatingLookUp> getUserSimilarity(User user) {
        List<RatingLookUp> similarityScore = new LinkedList<>();
        for (User other : userRepository.findAll()) {
            Long otherId = other.getId();

            if (!otherId.equals(user.getId())) {

                double cosineScore = calUserSim(user, other);

                if (cosineScore != -100.0) {
                    similarityScore.add(new RatingLookUp(otherId, cosineScore));
                }
            }
        }

        similarityScore.sort(Collections.reverseOrder());
        return similarityScore;
    }

    @Override
    public List<RatingLookUp> getSimilarRatings(User user) {
        List<RatingLookUp> similarityScore = getUserSimilarity(user);
        int numbNeighbors = Math.min(similarityScore.size(), similarityNum);
        List<RatingLookUp> similarityRatingList = new LinkedList<>();

        List<ReadBook> bookList = readBookRepository.findAll();


        RatingLookUp userRating;
        User userOther;
        long otherId, bookId;
        int counter;
        double norm, total, otherAvg, rating, cosineScore;
        double userAvgRating = getUserAvgRating(user);

        for (ReadBook book : bookList) {
            bookId = book.getBook().getId();
            counter = 0;
            norm = 0.0;
            total = 0.0;
            for (int i = 0; i < numbNeighbors; i++) {

                userRating = similarityScore.get(i);
                cosineScore = userRating.rating();
                otherId = userRating.id();
                userOther = userRepository.findById(otherId).orElse(null);

                if (userOther == null) {
                    continue;
                }

                otherAvg = getUserAvgRating(userOther);
                rating = book.getRating();

                if (rating != -1) {
                    counter++;
                    norm += Math.abs(cosineScore);
                    total += (rating - otherAvg) * cosineScore;
                }
            }
            if (counter >= minimalRater) {
                similarityRatingList.add(new RatingLookUp(bookId, userAvgRating + (total / norm)));
            }
        }
        similarityRatingList.sort(Collections.reverseOrder());
        return similarityRatingList;
    }
}
