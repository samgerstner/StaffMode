package pro.samgerstner.staffmode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLHelper
{
    public Connection connection;

    public SQLHelper(String host, String port, String database, String username, String password) throws SQLException
    {
        String conString = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        this.connection = DriverManager.getConnection(conString, username, password);
    }

    public void init() throws SQLException
    {
        String sql = """
            CREATE TABLE IF NOT EXISTS enable_log (
                id int PRIMARY KEY AUTO_INCREMENT,
                role varchar(255) NOT NULL,
                uuid varchar(36) NOT NULL,
                username varchar(255) NOT NULL,
                time TIMESTAMP NOT NULL
            );
    """;

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();

        sql = """
            CREATE TABLE IF NOT EXISTS disable_log (
                id int PRIMARY KEY AUTO_INCREMENT,
                role varchar(255) NOT NULL,
                uuid varchar(36) NOT NULL,
                username varchar(255) NOT NULL,
                time TIMESTAMP NOT NULL
            );
    """;

        ps = connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    public void logEnable(String uuid, String username, String role) throws SQLException
    {
        String sql = "INSERT INTO enable_log (role, uuid, username, time) VALUES (?, ?, ?, ?)";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, role);
        ps.setString(2, uuid);
        ps.setString(3, username);
        ps.setString(4, timestamp);
        ps.executeUpdate();
    }

    public void logDisable(String uuid, String username, String role) throws SQLException
    {
        String sql = "INSERT INTO disable_log (role, uuid, username, time) VALUES (?, ?, ?, ?)";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, role);
        ps.setString(2, uuid);
        ps.setString(3, username);
        ps.setString(4, timestamp);
        ps.executeUpdate();
    }
}