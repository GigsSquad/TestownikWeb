package pl.wropol.webapp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.wropol.model.Lecturer;
import pl.wropol.service.lecturer.LecturerService;
import pl.wropol.service.review.ReviewService;

import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Log4j
@Controller
public class HomeController {

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

    @RequestMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("lecturerList", allLecturers());
        return "index";
    }
}
