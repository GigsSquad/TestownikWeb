package pl.wropol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rafal on 2016-01-15.
 */

@Entity
@Getter
@Setter
public class Lecturer {

    @GeneratedValue
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "lecturer", cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne
    private University university;

    public Lecturer() {
    }

    public Lecturer(String name) {
        this.name = name;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
}
