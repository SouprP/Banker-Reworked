package me.souprpk.main.StorageSystem.MySQL;

import me.souprpk.main.Banker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Banker banker;

    private final String host = banker.getConfig().getString("host");
    private final String port = banker.getConfig().getString("port");
    private final String database = banker.getConfig().getString("database");
    private final String username = banker.getConfig().getString("username");
    private final String password = banker.getConfig().getString("password");

    private Connection connection;

    public boolean isConnected() {
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?useSSL=false",
                username, password);
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
