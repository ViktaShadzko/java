package ehu.java.cofffffeeeeee.repository;

import ehu.java.cofffffeeeeee.entity.Review;

import java.util.List;

public interface ReviewRepository {
    List<Review> getAllReviews();

    List<Review> getReviewsByBeverageId(long beverageId);

    Review getReviewById(long id);

    boolean addReview(Review review);

    Review deleteReview(long id);

    long getReviewCount();
}

