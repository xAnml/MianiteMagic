package net.anmlmc.mianitemagic.MySQL;

import net.anmlmc.mianitemagic.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

/**
 * Created by kishanpatel on 12/12/15.
 */
public class MySQL {

    public static FileConfiguration config = Main.getMain().getConfig();

    private static String host = config.getString("MySQL.Host");
    private static String port = config.getString("MySQL.Port");
    private static String database = config.getString("MySQL.Database");
    private static String username = config.getString("MySQL.Username");
    private static String password = config.getString("MySQL.Password");

    private static Connection connection;

    public MySQL() {

        try {
            if (connection != null) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public ResultSet getResultSet(String qry) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(qry);
        return statement.executeQuery();
    }

    public void executeUpdate(String qry) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(qry);
        statement.executeUpdate();
        statement.close();
    }

    public void createTables() {

        try {
            executeUpdate("CREATE TABLE IF NOT EXISTS `SpellPlayerInfo` (UUID VARCHAR(10), Mana INTEGER, " +
                    "GeneralLevel INTEGER, GeneralExperience INTEGER, " +
                    "OffensiveLevel INTEGER, OffensiveExperience INTEGER, " +
                    "DefensiveLevel INTEGER, DefensiveExperience INTEGER, " +
                    "ActiveSpell VARCHAR(50))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConfig() {

        String host = "MySQL.Host";
        String port = "MySQL.Port";
        String database = "MySQL.Database";
        String username = "MySQL.Username";
        String password = "MySQL.Password";

        if (!config.contains(host)) {
            config.set(host, "localhost");
        }

        if (!config.contains(port)) {
            config.set(port, "3306");
        }

        if (!config.contains(database)) {
            config.set(database, "database");
        }

        if (!config.contains(username)) {
            config.set(username, "username");
        }

        if (!config.contains(password)) {
            config.set(password, "password");
        }

        Main.getMain().saveConfig();

    }
}