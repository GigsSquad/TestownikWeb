package pl.wropol.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.wropol.model.Review;
import pl.wropol.service.ReviewService;
import pl.wropol.worker.ParserWorker;

import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Controller
public class HomeController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ParserWorker parserWorker;

    @RequestMapping(value = "/")
    public String home(Model model) {
        List<Review> reviewList = reviewService.findAll();
        model.addAttribute("reviews", reviewList);

        return "index";
    }

    @RequestMapping(value = "/parse")
    public String parser() {
        parserWorker.invoke();
        return "index";
    }
}
