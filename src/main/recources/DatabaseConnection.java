package main.recources;


import org.apache.commons.dbcp2.BasicDataSource;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_website";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

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
docker run --name university_task -e MYSQL_ROOT_PASSWORD=1234567890 -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -p 3306:3306 -d mysql
