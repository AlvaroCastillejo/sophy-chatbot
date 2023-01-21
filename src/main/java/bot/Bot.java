package bot;

import chatbot.Chatbot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    private HashMap<String, Chatbot> chatbotManager = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().getText().equals("/help")){
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText("Hola, mi nombre es Sophy, y soy un chatbot que trata temas filosóficos.");
            executeE(response);
            response.setText("Puedes hablar conmigo de cualquier tema que te apetezca, y si la conversación avanza te podré recomendar algún libro!");
            executeE(response);
            response.setText("Sólo recuerda que para hablar conmigo primero debes escribir el comando \"/start\". Para dejar de hablar conmigo puedes poner simplemente \"exit\".");
            executeE(response);
            return;
        }
        if(update.getMessage().getText().equals("/start")){
            // New user
            Chatbot sophia = new Chatbot(update,this);
            sophia.start();
            chatbotManager.put(update.getMessage().getChatId().toString(),sophia);
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText("Hola, parece que quieres que te recomiende algún libro... Pero antes hablemos un poco.");
            executeE(response);
            return;
        }
        if(chatbotManager.get(update.getMessage().getChatId().toString()) == null){
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText("*susurra* Ps! Hey! No has usado el comando \"/start\"...");
            executeE(response);
            return;
        }

        Chatbot sophy = chatbotManager.get(update.getMessage().getChatId().toString());
        sophy.setWaiting(false,update);
    }

    public void executeE(SendMessage c){
        try {
            execute(c);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        // TODO insert here your bot name
        return "";
    }

    @Override
    public String getBotToken() {
        // TODO insert here your bot token
        return "";
    }

    public void deleteUser(String id) {
        chatbotManager.remove(id);
    }
}
