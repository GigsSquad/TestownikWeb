package pl.wropol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by evelan on 1/15/16.
 */
@Entity
@Getter
@Setter
public class University {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private String city;

    @OneToMany(mappedBy = "university")
    private Set<Lecturer> lecturers = new HashSet<>();

    public void addLecturer(Lecturer lecturer) {
        this.lecturers.add(lecturer);
    }

}
