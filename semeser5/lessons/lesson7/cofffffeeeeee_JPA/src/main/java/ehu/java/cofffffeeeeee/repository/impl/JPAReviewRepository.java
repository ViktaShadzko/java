package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Review;
import ehu.java.cofffffeeeeee.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Profile("jpa")
@Transactional
public class JPAReviewRepository implements ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Review> getAllReviews() {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r", Review.class);
        return query.getResultList();
    }

    @Override
    public List<Review> getReviewsByBeverageId(long beverageId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.beverage.id = :beverageId", Review.class);
        query.setParameter("beverageId", beverageId);
        return query.getResultList();
    }

    @Override
    public Review getReviewById(long id) {
        return entityManager.find(Review.class, id);
    }

    @Override
    public boolean addReview(Review review) {
        try {
            if (review.getId() == null) {
                entityManager.persist(review);
            } else {
                entityManager.merge(review);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Review deleteReview(long id) {
        Review review = getReviewById(id);
        if (review != null) {
            entityManager.remove(review);
        }
        return review;
    }

    @Override
    public long getReviewCount() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM Review r", Long.class);
        return query.getSingleResult();
    }
}

