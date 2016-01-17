package pl.wropol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wropol.model.Lecturer;

import java.util.List;

/**
 * Created by Rafal on 2016-01-15.
 */
@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    Lecturer findAllByName(String name);

    List<Lecturer> findOneByNameLike(String name);
}
