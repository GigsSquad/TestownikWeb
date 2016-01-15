package pl.wropol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Rafal on 2016-01-15.
 */

@Entity
@Getter
@Setter
public class Lecturer {

    private String name;

    @OneToMany(mappedBy = "lecturer")
    private Set<Review> reviews;

    public Lecturer(String name){
        this.name = name;
    }
}
