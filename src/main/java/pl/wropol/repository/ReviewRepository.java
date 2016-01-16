package pl.wropol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wropol.model.Review;

import java.util.Date;

/**
 * Created by evelan on 1/15/16.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByTextAndPostDate(String text, Date postDate);
}
