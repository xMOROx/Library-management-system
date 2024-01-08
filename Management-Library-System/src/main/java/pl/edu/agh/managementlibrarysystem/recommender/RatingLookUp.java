package pl.edu.agh.managementlibrarysystem.recommender;

public record RatingLookUp(Long id, double rating) implements Comparable<RatingLookUp> {

    @Override
    public int compareTo(RatingLookUp o) {
        return Double.compare(o.rating(), this.rating());
    }
}
