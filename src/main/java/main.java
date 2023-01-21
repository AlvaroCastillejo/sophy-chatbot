import bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.Output;

public class main {
    public static void main(String[] args) {
        //ConnectorDB.connect();

        Output.print("   _____             _           \n" +
                "  / ____|           | |          \n" +
                " | (___   ___  _ __ | |__  _   _ \n" +
                "  \\___ \\ / _ \\| '_ \\| '_ \\| | | |\n" +
                "  ____) | (_) | |_) | | | | |_| |\n" +
                " |_____/ \\___/| .__/|_| |_|\\__, |\n" +
                "              | |           __/ |\n" +
                "              |_|          |___/ ", "green");

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        //Chatbot sophia = new Chatbot();
        //sophia.start();

        //ConnectorDB.closeCon();
    }
}
