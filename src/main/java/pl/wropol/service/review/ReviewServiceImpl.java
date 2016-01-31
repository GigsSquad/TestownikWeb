package pl.wropol.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wropol.model.Lecturer;
import pl.wropol.model.Review;
import pl.wropol.repository.LecturerRepository;
import pl.wropol.repository.ReviewRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;

    @Autowired
    LecturerRepository lecturerRepository;

    @Override
    @Cacheable(cacheNames = "reviews")
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "countReviews")
    public Long count() {
        return repository.count();
    }

    @Override
    @Cacheable(cacheNames = "review", key = "#id")
    public Review findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    @Transactional
    public void save(Review entity) {
        Review review = repository.findByTextAndPostDate(entity.getText(), entity.getPostDate());
        boolean reviewExist = review != null;
        if (!reviewExist) {
            entity.setCreated(new Date());
            repository.save(entity);
        }
    }

    @Override
    @Cacheable(cacheNames = "reviews", key = "#lecturer.name")
    public List<Review> findByLecturer(Lecturer lecturer) {
        return repository.findByLecturer(lecturer);
    }
}
