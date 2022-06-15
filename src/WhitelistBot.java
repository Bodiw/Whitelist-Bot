import bot.DiscordBot;
import db.DatabaseManager;

public class WhitelistBot {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Usage: java WhitelistBot <databaseDirectory> <botToken>");
        } else {
            DatabaseManager dm = DatabaseManager.getInstance(args[1]);

            dm.createDiscordPlayerTable();

            @SuppressWarnings("unused")
            DiscordBot bot = new DiscordBot(args[1]);
        }
    }
}
