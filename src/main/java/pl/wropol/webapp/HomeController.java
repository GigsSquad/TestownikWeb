package pl.wropol.webapp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @Autowired
    ParserWorker parserWorker;
    @Value("${cookie}")
    private String cookie;

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
        return "index";
    }

    @RequestMapping(value = "/run")
    public String runParser() {
        Thread t = new Thread(parserWorker);
        t.start();
        return "redirect:/";
    }

    //    @PostConstruct
    private void postConstructStart() {
        Thread t = new Thread(parserWorker);
        t.start();
    }
}
