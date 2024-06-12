package assignment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MyArticleService implements ArticleService{
    @Override
    public ArrayList<String> getlinks(String url) {
            HashSet<String> links = new HashSet<String>();
            try{
                Document document = Jsoup.connect(url).get();
                Elements elements = document.getElementsByTag("a");
                for (Element element : elements) {
                    String href = element.attr("href");
                    if(href.contains("https://vietnamnet.vn/") && href.contains(".html")){
                        links.add(href);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ArrayList<>(links);
    }

    @Override
    public Article getArticle(String url) {
        Document doc = null;
        try{
            doc = Jsoup.connect(url).get();
            String title = doc.select("h1.content-detail-title").text();
            String description = doc.select("h2..content-detail-sapo sm-sapo-mb-0").text();
            String thumbnail = doc.select("picture img[itemprop=contentUrl]").attr("data-src");
            String content = doc.select("p.maincontent main-content").text();
            Article article = new Article();
            article.setTitle(title);
            article.setDescription(description);
            article.setThumbnail(thumbnail);
            article.setContent(content);
            return article;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
