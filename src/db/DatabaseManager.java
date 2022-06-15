package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static String DATABASE;
    private static DatabaseManager instance;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static DatabaseManager getInstance(String database) {
        DatabaseManager instance = getInstance();
        DATABASE = "jdbc:sqlite:" + database;
        return instance;
    }

    public void createDiscordPlayerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS discordUser_minecraftPlayer (dcid TEXT PRIMARY KEY, uuid TEXT, banned INTEGER)";

        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUUIDFromDiscordID(String discordID) {
        String sql = "SELECT uuid FROM discordUser_minecraftPlayer WHERE dcid = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, discordID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDiscordIDFromUUID(String uuid) {
        String sql = "SELECT dcid FROM discordUser_minecraftPlayer WHERE uuid = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("dcid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeFromWhitelist(String uuid) {
        String sql = "DELETE FROM whitelist WHERE uuid = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToWhitelist(String uuid, String username, int whitelisted) {
        String sql = "INSERT OR REPLACE INTO whitelist (uuid, name, whitelisted) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.setString(2, username);
            stmt.setInt(3, whitelisted);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserBanned(String uuid) {
        String sql = "SELECT banned FROM discordUser_minecraftPlayer WHERE uuid = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("banned") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayerBanned(String dcid) {
        String sql = "SELECT banned FROM discordUser_minecraftPlayer WHERE dcid = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dcid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("banned") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromUUID(String uuid) {
        String sql = "SELECT name FROM whitelist WHERE uuid = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUserToDatabase(String dcid, String username, String uuid, int whitelisted) {
        String dcid2 = getDiscordIDFromUUID(uuid);

        if (dcid2 != null && !dcid2.equals(dcid)) {
            return false;
        }

        String uuidOld = getUUIDFromDiscordID(dcid);

        removeFromWhitelist(uuidOld);
        addToWhitelist(uuid, username, 1);

        String sql = "INSERT OR REPLACE INTO discordUser_minecraftPlayer (dcid, uuid, banned) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dcid);
            stmt.setString(2, uuid);
            stmt.setInt(3, whitelisted == 1 ? 0 : 1);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
