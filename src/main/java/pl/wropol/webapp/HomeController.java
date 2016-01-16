package pl.wropol.webapp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Value("${cookie}")
    private String cookie;

    @RequestMapping(value = "/")
    public String home(Model model) {
        Long reviewCount = reviewService.count();
        Long lecturerCount = lecturerService.count();
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("lecturerCount", lecturerCount);
        model.addAttribute("cookie", cookie);

        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(@ModelAttribute(value = "cookie") String newCookie) {
        log.info(newCookie);
        cookie = newCookie;

        return "redirect:/";
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
