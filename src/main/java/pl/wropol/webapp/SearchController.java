package pl.wropol.webapp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wropol.model.Lecturer;
import pl.wropol.model.Review;
import pl.wropol.service.lecturer.LecturerService;
import pl.wropol.service.review.ReviewService;

import java.util.List;

/**
 * Created by evelan on 1/17/16.
 */
@Log4j
@Controller
public class SearchController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    LecturerService lecturerService;

    @ModelAttribute
    public List<Lecturer> allLecturers() {
        return lecturerService.findAll();
    }

    @ModelAttribute(value = "reviewCount")
    public Long reviewCount() {
        return reviewService.count();
    }

    @ModelAttribute(value = "lecturerCount")
    public Long lecturerCount() {
        return lecturerService.count();
    }

    @RequestMapping(value = "/search/lecturer", method = RequestMethod.GET)
    public String searchLecturer(@RequestParam(value = "q") String lecturerName, Model model) {
        lecturerName = lecturerName.replace("+", " ");
        lecturerName = "%" + lecturerName + "%";
        List<Lecturer> lecturerList = lecturerService.findOneByNameLike(lecturerName);
        model.addAttribute("lecturerList", lecturerList);
        return "index";
    }

    @RequestMapping(value = "/search/review", method = RequestMethod.GET)
    public String searchReviews(@RequestParam(value = "id") String lecturerId, Model model) {
        Lecturer lecturer = lecturerService.findOne(Long.valueOf(lecturerId));
        List<Review> reviewList = reviewService.findByLecturer(lecturer);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("lecturerName", lecturer.getName());
        return "index";
    }

}
