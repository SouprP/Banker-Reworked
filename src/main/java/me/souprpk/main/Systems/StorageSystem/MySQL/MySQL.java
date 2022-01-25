package me.souprpk.main.Systems.StorageSystem.MySQL;

import me.souprpk.main.Banker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Banker banker;

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public MySQL(Banker banker) {
        this.banker = banker;
        host = banker.getConfig().getString("main.mysql.host");
        port = banker.getConfig().getString("main.mysql.port");
        database = banker.getConfig().getString("main.mysql.database");
        username = banker.getConfig().getString("main.mysql.username");
        password = banker.getConfig().getString("main.mysql.password");
    }

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
