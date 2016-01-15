package pl.wropol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wropol.model.Review;
import pl.wropol.repository.ReviewRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Service
public class ReviewService {

    @Autowired
    ReviewRepository repository;

    public List<Review> findAll() {
        return repository.findAll();
    }

    public Review findOne(Long id) {
        return repository.findOne(id);
    }

    public void save(Review entity) {
        repository.save(entity);
    }

    public void save(String nameOfTutor, Double rating, String text) {
        Review review = new Review(nameOfTutor, rating, text);
        review.setCreated(new Date());
        repository.save(review);
    }

}
