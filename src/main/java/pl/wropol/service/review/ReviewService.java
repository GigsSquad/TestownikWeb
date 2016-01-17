package pl.wropol.service.review;

import pl.wropol.model.Lecturer;
import pl.wropol.model.Review;
import pl.wropol.service.IService;

import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
public interface ReviewService extends IService<Review> {
    List<Review> findByLecturer(Lecturer lecturer);
}
