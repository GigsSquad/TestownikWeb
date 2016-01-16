package pl.wropol.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
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
}
