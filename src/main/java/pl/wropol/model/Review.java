package pl.wropol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(length = 100000000)
    private String text;

    private Double rating;

    private Date created;

    private Date postDate;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private boolean stolen;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private Lecturer lecturer;



    public Review() {

    }
}
