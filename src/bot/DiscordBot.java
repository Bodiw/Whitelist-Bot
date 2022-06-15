package bot;

import javax.security.auth.login.LoginException;

import bot.commands.ProfileCommand;
import bot.commands.WhitelistCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot {

    public DiscordBot(String token) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault(token)
                // Disable parts of the cache
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                // Enable the bulk delete event
                .setBulkDeleteSplittingEnabled(false)
                // Set activity (like "playing Something")
                .setActivity(Activity.watching("Minecraft Saw Games"))

                .addEventListeners(new ProfileCommand(), new WhitelistCommand())

                .build();

        jda.awaitReady();
        jda.upsertCommand("profile", "shows your current profile").queue();
        jda.upsertCommand("whitelist", "gets you whitelisted or replaces your current whitelist").addOption(OptionType.STRING, "username", "minecraft username").queue();

        System.out.println("Bot is now online!");
    }
}
