package ehu.java.cofffffeeeeee.controller;

import ehu.java.cofffffeeeeee.entity.Review;
import ehu.java.cofffffeeeeee.repository.ReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        log.info("Fetching all reviews");
        List<Review> reviews = reviewRepository.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable long id) {
        log.info("Fetching review with id: {}", id);
        Review review = reviewRepository.getReviewById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    @GetMapping("/beverage/{beverageId}")
    public ResponseEntity<List<Review>> getReviewsByBeverageId(@PathVariable long beverageId) {
        log.info("Fetching reviews for beverage with id: {}", beverageId);
        List<Review> reviews = reviewRepository.getReviewsByBeverageId(beverageId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@Valid @RequestBody Review review) {
        log.info("Adding new review: {}", review);
        boolean success = reviewRepository.addReview(review);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable long id) {
        log.info("Deleting review with id: {}", id);
        Review deleted = reviewRepository.deleteReview(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Review deleted successfully");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getReviewCount() {
        log.info("Fetching review count");
        long count = reviewRepository.getReviewCount();
        return ResponseEntity.ok(count);
    }
}

