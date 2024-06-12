package assignment;

import java.sql.*;
import java.util.ArrayList;

public class MySqlArticleRepository implements ArticleRepository {

        private Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/news_db";
            String username = "root";
            String password = "";
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public ArrayList<Article> findAll() {
            ArrayList<Article> articles = new ArrayList<>();
            String query = "SELECT * FROM articles";

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Article article = new Article();
                    articles.add(article);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return articles;
        }

        @Override
        public Article findByUrl(String url) {
            Article article = null;
            String query = "SELECT * FROM articles WHERE baseUrl = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, url);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    article = new Article();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return article;
        }

        @Override
        public Article save(Article article) {
            String query = "INSERT INTO articles (baseUrl, title, description, content, thumbnail, createdAt, updatedAt, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                // set parameters
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    article.setId(keys.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return article;
        }

        @Override
        public Article update(Article article) {
            String query = "UPDATE articles SET title = ?, description = ?, content = ?, thumbnail = ?, updatedAt = ?, status = ? WHERE id = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return article;
        }

        @Override
        public void deleteByUrl(String url) {
            String query = "DELETE FROM articles WHERE baseUrl = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, url);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

