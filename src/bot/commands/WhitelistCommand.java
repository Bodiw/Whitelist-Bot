package bot.commands;

import db.DatabaseManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Embeds;
import util.MinecraftAPI;

public class WhitelistCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("whitelist")) {

            if (event.getOptions().size() == 0) {
                event.reply("Usage: /whitelist <username>").setEphemeral(true).queue();
                return;
            }

            DatabaseManager dm = DatabaseManager.getInstance();

            String dcid = event.getUser().getId();
            String username = event.getOption("username").getAsString();
            String uuid = MinecraftAPI.getUUIDFromUsername(username);

            if (uuid == null) {
                event.reply("Could not find UUID for " + username + ", is that username correct?").setEphemeral(true).queue();
                ;
                return;
            }

            if (dm.isUserBanned(dcid) || dm.isPlayerBanned(dcid)) {
                event.reply("User " + event.getUser().getName() + " or player " + username + " is banned.").setEphemeral(true).queue();
                ;
                return;
            }

            boolean success = dm.addUserToDatabase(dcid, username, uuid, 1);
            if (success) {
                System.out.println("Added " + username + " to whitelist");
                event.replyEmbeds(Embeds.getProfileEmbed(username, uuid, true)).setEphemeral(true).queue();
            } else {
                event.reply("Could not add " + username + " to whitelist, did you try to clain someone else's player?").setEphemeral(true).queue();
            }
        }
    }
}