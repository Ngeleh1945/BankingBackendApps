//package org.example.util;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class ConnectionDB {
//    private static final HikariDataSource dataSource;
//
//    static {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/banking");
//        config.setUsername("root");
//        config.setPassword("");
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        config.setMaximumPoolSize(20);
//        config.setMinimumIdle(5);
//        config.setIdleTimeout(300000);
//        config.setConnectionTimeout(20000);
//
//        dataSource = new HikariDataSource(config);
//    }
//
//    public static Connection connect() throws SQLException{
//        return dataSource.getConnection();
//    }
//}
