package assignment;

import java.util.ArrayList;

public interface ArticleService {
    ArrayList<String> getlinks (String url);
    Article getArticle (String url);
}
