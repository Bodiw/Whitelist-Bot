package bot.commands;

import db.DatabaseManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Embeds;

public class ProfileCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("profile")) {

            DatabaseManager dm = DatabaseManager.getInstance();

            String dcid = event.getUser().getId();
            String uuid = dm.getUUIDFromDiscordID(dcid);
            String username = dm.getUsernameFromUUID(uuid);
            boolean whitelisted = !(dm.isPlayerBanned(dcid) || dm.isUserBanned(uuid));

            if (uuid == null) {
                event.reply("Could not find your player profile, have you whitelisted yourself?").setEphemeral(true).queue();
                return;
            }

            MessageEmbed embed = Embeds.getProfileEmbed(username, uuid, whitelisted);

            event.replyEmbeds(embed).setEphemeral(true).queue();;
        }
    }
}