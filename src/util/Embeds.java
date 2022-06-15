package util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Embeds {

    public static MessageEmbed getProfileEmbed(String username, String uuid, boolean whitelisted) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(username).setDescription("UUID: " + uuid + "\nWhitelisted: " + (whitelisted ? "Yes" : "No")).setColor(whitelisted ? 0x00ff00 : 0xff0000).setThumbnail("https://crafatar.com/avatars/" + uuid + "?overlay");
        return eb.build();
    }

}
