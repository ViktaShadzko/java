package ehu.java.cofffffeeeeee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("BEVERAGE")
public class Beverage {
    @Id
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Price must be positive")
    private double price;

    private String description;

    public Beverage(String name, long id, double price, String description) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.description = description;
    }
}

