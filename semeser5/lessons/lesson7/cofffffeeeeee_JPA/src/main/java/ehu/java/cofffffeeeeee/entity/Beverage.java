package ehu.java.cofffffeeeeee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BEVERAGE")
public class Beverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private double price;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "beverage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Beverage(String name, long id, double price, String description) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.description = description;
    }

    public Beverage(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setBeverage(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setBeverage(null);
    }
}

