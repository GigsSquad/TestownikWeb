package pl.wropol.webapp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.wropol.service.lecturer.LecturerService;
import pl.wropol.service.review.ReviewService;
import pl.wropol.worker.ParserWorker;

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

    @RequestMapping(value = "/")
    public String home(Model model) {
        Long reviewCount = reviewService.count();
        Long lecturerCount = lecturerService.count();
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("lecturerCount", lecturerCount);
        return "index";
    }

    @RequestMapping(value = "/run")
    public String runParser() {
        Thread t = new Thread(parserWorker);
        t.start();
        return "redirect:/";
    }

    @Autowired
    ParserWorker parserWorker;

    //    @PostConstruct
    private void postConstructStart() {
        Thread t = new Thread(parserWorker);
        t.start();
    }
}
