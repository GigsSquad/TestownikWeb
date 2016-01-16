package pl.wropol.worker;

import lombok.extern.log4j.Log4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.wropol.model.ActivityType;
import pl.wropol.model.Lecturer;
import pl.wropol.model.Review;
import pl.wropol.service.lecturer.LecturerService;
import pl.wropol.service.review.ReviewService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Component
@Log4j
public class ParserWorker implements Runnable {

    @Autowired
    ReviewService reviewService;

    @Autowired
    LecturerService lecturerService;

    private String BASE_URL = "http://polwro.pl/";
    private String LINK_TO_GROUPS = BASE_URL + "index.php?c=6";
    private int NEXT_TOPICS_PAGE = 50;
    private int NEXT_REVIEW_PAGE = 25;
    private String userAgent = "Chrome/41.0.2228.0";

    private String CONDITION_TO_WAIT = "odśwież stronę, aby przeglądać wybrane opinie o prowadzących.";
    private long INTERVAL = 600;

    @Value("${cookie}")
    String cookie;

    private void login() {
        try {
            Connection.Response res = Jsoup.connect("http://polwro.pl/login.php?redirect=")
                    .data("username", "evelan")
                    .data("password", "123456789")
                    .data("redirect", "")
                    .data("login", "Zaloguj")
                    .method(Connection.Method.POST)
                    .userAgent(userAgent)
                    .execute();

            log.info(res.cookies());
            cookie = res.cookie("bb038dfef1_sid");
            log.info(cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void invoke() {
        login();
        log.info("Parsowanie...");
        for (String linkToGroup : getLinksOfGroups()) {
            Elements topics = getAllTopics(linkToGroup);
            log.info("LinkToGroup: " + linkToGroup);
            for (Element tutor : topics) {
                log.info("Prowadzacy: " + tutor.text());
                if (tutor.text().length() > 30)
                    continue;

                String lecturerName = tutor.text();
                Lecturer lecturer = lecturerService.findOneByName(lecturerName);
                boolean notExistInDatabase = lecturer == null;
                if (notExistInDatabase) {
                    lecturer = new Lecturer(lecturerName);
                    lecturerService.save(lecturer);
                }

                int page = 0;
                boolean pageExist = true;
                while (pageExist) {
                    String specifiedLink = tutor.attr("href");
                    String link = createUrl(specifiedLink, page);
                    Document doc = getDocument(link);
                    Elements ratings = doc.select("[itemprop=ratingValue]");
                    Elements reviewBody = doc.select("[itemprop=reviewBody]");
                    Elements postDateEl = doc.getElementsByClass("post_date");

                    pageExist = ratings.size() > 2;
                    int numberOfReviews = ratings.size();

                    for (int i = 0; i < numberOfReviews; i++) {
                        Double rating = Double.valueOf(ratings.get(i).text().replace(",", "."));
                        String text = reviewBody.get(i).text();

                        Date postDate = getDate(postDateEl.get(i).text());
                        ActivityType activityFromText = getActivity(text);

                        Review review = new Review();
                        review.setRating(rating);
                        review.setText(text);
                        review.setStolen(true);
                        review.setActivityType(activityFromText);
                        review.setPostDate(postDate);
                        review.setActivityType(ActivityType.OTHER);
                        review.setLecturer(lecturer);
                        reviewService.save(review);

                    }
                    page += NEXT_REVIEW_PAGE;
                }
            }
        }
    }

    private ActivityType getActivity(String text) {
        if (text.contains("ćwiczenia"))
            return ActivityType.EXE;
        else if (text.contains("lab"))
            return ActivityType.LAB;
        else if (text.contains("wykład"))
            return ActivityType.LEC;
        else if (text.contains("seminarium"))
            return ActivityType.SEM;
        else if (text.contains("projekt"))
            return ActivityType.PRO;
        else
            return ActivityType.OTHER;

    }

    private Date getDate(String date) {
        String[] dateInString = date.split(",");

        date = dateInString[0] + " " + dateInString[1];

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date result = null;
        try {
            result = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        return result;
    }

    /**
     * view-source:http://polwro.pl/index.php?c=6
     * href="/f,matematycy,6"
     *
     * @return /f,matematycy,6
     */
    private List<String> getLinksOfGroups() {
        List<String> linkToGroup = new ArrayList<>();
        Document document = getDocument(LINK_TO_GROUPS);
        Elements elementsByClass = document.getElementsByClass("fnorm");
        for (Element byClass : elementsByClass) {
            byClass = byClass.select("a").first();
            String partialLink = byClass.attr("href");
            String linkOfGroup = "http://polwro.pl" + partialLink;
            linkToGroup.add(linkOfGroup);
        }
        return linkToGroup;
    }

    private Elements getAllTopics(String baseLink) {
        int page = 0;
        Elements allTopics = new Elements();
        boolean pageExist = true;
        while (pageExist) {
            String link = baseLink + "?start=" + page;
//            System.out.println("Topic url: " + link);
            Document doc = downloadDocument(link);

            Elements partOfTopics = doc.getElementsByClass("vf");
            pageExist = partOfTopics.size() > 10;
            if (pageExist) {
                allTopics.addAll(partOfTopics);
            }
            page += NEXT_TOPICS_PAGE;
        }
        return allTopics;
    }

    private String createUrl(String link, int startPage) {
        return BASE_URL + "t," + link + "&postdays=0&postorder=asc?start=" + startPage;
    }

    private Document getDocument(String link) {
        Document doc = downloadDocument(link);
        while (needWait(doc)) {
            System.out.println("Need wait...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doc = downloadDocument(link);
        }
        return doc;
    }

    private boolean needWait(Document doc) {
        String body = doc.body().text();
        return body.contains(CONDITION_TO_WAIT);
    }

    private Document downloadDocument(String link) {
        try {
            Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = Jsoup.connect(link).userAgent(userAgent).cookie("bb038dfef1_sid", cookie).timeout(10 * 1000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public void run() {
        invoke();
    }
}

