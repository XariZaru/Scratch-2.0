package database;

import com.zaxxer.hikari.HikariDataSource;
import constants.ScratchConstants;

import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseConnection {

    private static final HikariDataSource source = new HikariDataSource();
    
    static {
        source.setMaximumPoolSize(20);
        source.setDriverClassName("org.mariadb.jdbc.Driver");
        source.setJdbcUrl(ScratchConstants.DB_URL);
        source.addDataSourceProperty("user", ScratchConstants.DB_USER);
        source.addDataSourceProperty("password", ScratchConstants.DB_PASS);
        source.setAutoCommit(true);
        
//    	source.setUrl(net.constants.ScratchConstants.DB_URL);
//    	source.setUser(net.constants.ScratchConstants.DB_USER);
//    	source.setPassword(net.constants.ScratchConstants.DB_PASS);
    }
      

    public static Connection getConnection() throws SQLException {
        return source.getConnection();        
    }

}
