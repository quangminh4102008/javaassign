package assignment;

import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static ArticleRepository articleRepository = new MySqlArticleRepository();
    private static ArticleService vnexpressService = new VnexpressArticleService();
    private static ArticleService myService = new MyArticleService();

    public static void generateMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Crawl thông tin từ vnexpress.");
            System.out.println("2. Crawl thông tin từ nguồn của tôi.");
            System.out.println("3. Hiển thị danh sách tin hiện có.");
            System.out.println("4. Thoát chương trình.");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    crawlFromVnexpress();
                    break;
                case 2:
                    crawlFromMySource();
                    break;
                case 3:
                    displayArticles();
                    break;
                case 4:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void crawlFromVnexpress() {
        String url = "https://vnexpress.net";
        ArrayList<String> links = vnexpressService.getlinks(url);
        for (String link : links) {
            Article article = vnexpressService.getArticle(link);
            articleRepository.save(article);
        }
    }

    private static void crawlFromMySource() {
        String url = "https://mynews.com";
        ArrayList<String> links = myService.getlinks(url);
        for (String link : links) {
            Article article = myService.getArticle(link);
            articleRepository.save(article);
        }
    }

    private static void displayArticles() {
        ArrayList<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            System.out.println(article);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Nhập mã tin cần xem chi tiết hoặc 'exit' để thoát:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int id = Integer.parseInt(input);
                Article article = articles.stream()
                        .filter(a -> a.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (article != null) {
                    System.out.println(article);
                } else {
                    System.out.println("Không tìm thấy bài viết với mã tin đã nhập.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Mã tin không hợp lệ. Vui lòng nhập lại.");
            }
        }
    }

    public static void main(String[] args) {
        generateMenu();
    }
}
