package resources;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_website";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "andriagio10"; //replace with your password;

    private static final BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}

