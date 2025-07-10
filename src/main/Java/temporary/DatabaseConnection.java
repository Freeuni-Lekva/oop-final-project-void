package temporary;


import org.apache.commons.dbcp2.BasicDataSource;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_website";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "1234567890";

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
