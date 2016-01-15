package pl.wropol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by evelan on 1/15/16.
 */
@Entity
@Getter
@Setter
public class Review {

    @GeneratedValue
    @Id
    private Long id;

    private String lecturer;

    @Column(length = 100000000)
    private String text;

    private Double rating;

    private Date created;

    public Review(String nameOfTutor, Double rating, String text) {
        this.lecturer = nameOfTutor;
        this.text = text;
        this.rating = rating;
    }

    public Review() {

    }
}
