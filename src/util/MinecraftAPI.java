package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class MinecraftAPI {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) {
        JSONObject json = null;
        try (InputStream is = new URL(url).openStream(); BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
            String jsonText = readAll(rd);
            if (jsonText.isEmpty()) {
                return null;
            }
            json = new JSONObject(jsonText);
        } catch (Exception e) {
        }
        return json;
    }

    // get the UUID from a username using mojang's minecraft api
    public static String getUUIDFromUsername(String username) {
        String uuid = null;
        JSONObject json = readJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (json != null) {
            uuid = json.getString("id");
            uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
        }
        return uuid;
    }

    public static String getUsernameFromUUID(String uuid) {
        String username = null;
        JSONObject json = readJsonFromUrl("https://api.mojang.com/user/profile/" + uuid);
        if (json != null) {
            username = json.getString("name");
        }
        return username;
    }
}
