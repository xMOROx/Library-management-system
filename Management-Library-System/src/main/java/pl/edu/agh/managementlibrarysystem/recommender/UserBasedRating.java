package pl.edu.agh.managementlibrarysystem.recommender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.ReviewBook;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.repository.ReviewBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;
import pl.edu.agh.managementlibrarysystem.utils.Pair;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("singleton")
public class UserBasedRating extends SimilarityRatingCal {

    private final HashMap<Long, Pair<List<ReviewBook>, LocalDateTime>> cachedRepositories = new HashMap<>();
    private final int timeDeltaInMinutes = 60;
    private double userAvgRating;
    private double otherAvgRating;

    protected UserBasedRating(ReviewBookRepository readBookRepository,
                              UserRepository userRepository,
                              @Value("50") int similarityNum,
                              @Value("1") int minimalRater) {
        super(readBookRepository, userRepository);
        this.similarityNum = similarityNum;
        this.minimalRater = minimalRater;

    }

    @Override
    public List<RatingLookUp> getSimilarRatings(User user) {
        List<RatingLookUp> similarityRatingList = new LinkedList<>();

        List<RatingLookUp> similarityScore = getUserSimilarity(user);
        List<ReviewBook> bookList = readBookRepository.findAll();

        int numbNeighbors = Math.min(similarityScore.size(), similarityNum);

        UserToOtherSimilarityRating userToOtherSimilarityRating;
        userAvgRating = getUserAvgRating(user);

        for (ReviewBook book : bookList) {
            userToOtherSimilarityRating = new UserToOtherSimilarityRating();
            userToOtherSimilarityRating.calculateRating(numbNeighbors, book, similarityScore);

            if (userToOtherSimilarityRating.isRatingAchieveMinimalRaterRequirements(this.minimalRater)) {
                userToOtherSimilarityRating.addNewRating(similarityRatingList, book.getBook().getId());
            }
        }
        similarityRatingList.sort(Comparator.reverseOrder());
        return similarityRatingList;
    }

    private double calUserSim(User user, User other) {
        userAvgRating = getUserAvgRating(user);
        otherAvgRating = getUserAvgRating(other);
        List<ReviewBook> userBooks = getUserBooks(user);
        List<ReviewBook> otherBooks = getUserBooks(other);

        var userSimilarityStatistics = new UserToOtherSimilarityStatistics();

        for (ReviewBook book : userBooks) {
            for (ReviewBook otherBook : otherBooks) {
                if (isBookIdAndOtherBookIdEquals(book, otherBook)) {
                    updateUserSimilarityStatistics(userSimilarityStatistics, book, otherBook);
                }
            }
        }

        if (isUserSimilarityStatisticsMatchRequirements(userSimilarityStatistics)) {
            return calculateScore(userSimilarityStatistics);
        }

        return -100.0;
    }

    private boolean isBookIdAndOtherBookIdEquals(ReviewBook book, ReviewBook otherBook) {
        return book.getBook().getId().equals(otherBook.getBook().getId());
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

    private void updateUserSimilarityStatistics(UserToOtherSimilarityStatistics userSimilarityStatistics, ReviewBook book, ReviewBook otherBook) {
        userSimilarityStatistics.minNumCommon++;
        userSimilarityStatistics.userScore = book.getRating() - userAvgRating;
        userSimilarityStatistics.otherScore = otherBook.getRating() - otherAvgRating;
        userSimilarityStatistics.similarityScore += (userSimilarityStatistics.userScore) * (userSimilarityStatistics.otherScore);
        userSimilarityStatistics.nomUser += Math.pow(userSimilarityStatistics.userScore, 2);
        userSimilarityStatistics.nomOther += Math.pow(userSimilarityStatistics.otherScore, 2);
    }

    private double getUserAvgRating(User user) {
        return getUserBooks(user)
                .stream()
                .mapToDouble(ReviewBook::getRating)
                .average()
                .orElse(0.0);
    }

    private List<ReviewBook> getUserBooks(User user) {
        Long userId = user.getId();
        if (cachedRepositories.containsKey(userId)) {
            if (cachedRepositories.get(userId).getSecond().isAfter(LocalDateTime.now().minusMinutes(timeDeltaInMinutes))) {
                return cachedRepositories.get(userId).getFirst();
            }
            removeObsoleteCache();
        }

        updateUserBooksCache(user);
        return cachedRepositories.get(userId).getFirst();
    }

    private void updateUserBooksCache(User user) {
        Long userId = user.getId();
        List<ReviewBook> userBooks = readBookRepository.findAllByUserId(userId);
        cachedRepositories.put(userId, Pair.build(userBooks, LocalDateTime.now()));
    }

    private void removeObsoleteCache() {
        LocalDateTime now = LocalDateTime.now();
        cachedRepositories.entrySet().removeIf(entry -> entry.getValue().getSecond().isBefore(now.minusMinutes(timeDeltaInMinutes)));
    }

    private boolean isUserSimilarityStatisticsMatchRequirements(UserToOtherSimilarityStatistics userSimilarityStatistics) {
        return userSimilarityStatistics.minNumCommon >= 2
                && userSimilarityStatistics.similarityScore != 0.0
                && userSimilarityStatistics.nomUser != 0.0
                && userSimilarityStatistics.nomOther != 0.0;
    }

    private double calculateScore(UserToOtherSimilarityStatistics userSimilarityStatistics) {
        return userSimilarityStatistics.similarityScore / (Math.sqrt(userSimilarityStatistics.nomUser * userSimilarityStatistics.nomOther));
    }

    private static class UserToOtherSimilarityStatistics {
        private double similarityScore = 0.0;
        private double nomUser = 0.0;
        private double nomOther = 0.0;
        private int minNumCommon = 0;
        private double userScore, otherScore;
    }

    private class UserToOtherSimilarityRating {
        private double norm;
        private double total;
        private int counter;

        public UserToOtherSimilarityRating() {
            this.norm = 0.0;
            this.total = 0.0;
            this.counter = 0;
        }

        public void calculateRating(int numbNeighbors, ReviewBook book, List<RatingLookUp> similarityScore) {
            RatingLookUp userRating;
            User userOther;
            for (int i = 0; i < numbNeighbors; i++) {

                userRating = similarityScore.get(i);
                double cosineScore = userRating.rating();
                long otherId = userRating.id();
                userOther = userRepository.findById(otherId).orElse(null);

                if (userOther == null) {
                    continue;
                }

                double otherAvg = getUserAvgRating(userOther);
                double rating = book.getRating();

                if (rating != -1) {
                    counter++;
                    norm += Math.abs(cosineScore);
                    total += (rating - otherAvg) * cosineScore;
                }
            }
        }

        public boolean isRatingAchieveMinimalRaterRequirements(int minimal) {
            return counter >= minimal;
        }

        public void addNewRating(List<RatingLookUp> similarityRatingList, long bookId) {
            similarityRatingList.add(new RatingLookUp(bookId, userAvgRating + (this.total / this.norm)));
        }

    }
}
