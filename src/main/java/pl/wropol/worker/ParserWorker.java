package pl.wropol.worker;

import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.wropol.service.ReviewService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
@Component
@Log4j
public class ParserWorker implements Runnable {

    @Autowired
    ReviewService reviewService;

    private String BASE_URL = "http://polwro.pl/";
    private String LINK_TO_GROUPS = BASE_URL + "index.php?c=6";
    private int NEXT_TOPICS_PAGE = 50;
    private int NEXT_REVIEW_PAGE = 25;

    private String CONDITION_TO_WAIT = "odśwież stronę, aby przeglądać wybrane opinie o prowadzących.";
    private long INTERVAL = 600;

    public void invoke() {

        for (String linkToGroup : getLinksOfGroups()) {
//            System.out.println("Group link: " + linkToGroup);

            Elements topics = getAllTopics(linkToGroup);
            for (Element tutor : topics) {
                log.info("Prowadzacy: " + tutor.text());
//                System.out.println("Prowadzacy: " + tutor.text());

                int page = 0;
                boolean pageExist = true;
                while (pageExist) {
                    String specifiedLink = tutor.attr("href");
                    String link = createUrl(specifiedLink, page);
                    Document doc = getDocument(link);
                    Elements ratings = doc.select("[itemprop=ratingValue]");
                    Elements reviewBody = doc.select("[itemprop=reviewBody]");

                    pageExist = ratings.size() > 2;
                    int numberOfReviews = ratings.size();

                    for (int i = 0; i < numberOfReviews; i++) {
                        String nameOfTutor = tutor.text();
                        Double rating = Double.valueOf(ratings.get(i).text().replace(",", "."));
                        String text = reviewBody.get(i).text();

                        reviewService.save(nameOfTutor, rating, text);
                    }
                    page += NEXT_REVIEW_PAGE;
                }
            }
        }
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
            document = Jsoup.connect(link).userAgent("Chrome/41.0.2228.0").cookie("bb038dfef1_sid", "880898463ea69e710d43e135c6571506").timeout(10 * 1000).get();
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

