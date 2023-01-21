package chatbot;

import database.ConnectorDB;
import database.Dictionary;
import model.Book;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Output;
import utils.Rand;
import utils.Utils;
import bot.Bot;

import java.util.HashMap;
import java.util.LinkedList;

public class Chatbot extends Thread {
    private final Bot bot;
    private Update telegramChat;
    private static final int MIN_FEELING = 5;
    private static final int MIN_TAGS = 2;
    private static int feelingCount = 0;
    private static int VALUE_INCREMENT = 3;
    private static int LOG_BASE = 10;
    private static int PROB_CONV = 80;
    private static boolean DEBUG_MODE = true;
    private LinkedList<String> tags;
    private HashMap<String,Integer> tagsValue;
    private static LinkedList<String> usedQuotes = new LinkedList<>();
    private boolean waiting = true;
    private String username;

    public Chatbot(Update update, Bot bot) {
        telegramChat = update;
        this.bot = bot;
        this.username = update.getMessage().getFrom().getFirstName();
        if(DEBUG_MODE){
            Output.print("New user connected.", "green");
            Output.print("\tUsername: " + username, "green");
        }
    }

    public void startBot() {
        tags = new LinkedList<>();
        tagsValue = new HashMap<>();
        double porciento= 0;
        String finalanswer = null;
        Dictionary.initializeDatabase();

        response("Hola, de que quieres hablar hoy?");

        while (true){
            //Obtener input de usuario
            String answer = this.waitAnswer();
            Output.print(username + " - " + answer, "white");
            if(answer.equals("exit")){
                response("Ha sido un placer hablar contigo! Recuerda que siempre puedes retomar la conversación escribiendo \"/start\".");
                break;
            }
            answer = Utils.correctSentence(answer);

            feelingCount++;

            //Obtener los tags que conoce Sophy
            LinkedList<String> NewTags = Dictionary.getKnownTags(answer);
            for(String nt : NewTags){
                if(DEBUG_MODE) Output.print(nt,"yellow");
                if(!tags.contains(nt)){
                    tags.add(nt);
                    tagsValue.put(nt, Dictionary.getKeywordWeight().get(nt));
                } else {
                    tagsValue.replace(nt, tagsValue.get(nt), tagsValue.get(nt)+3);
                }
            }
            tags = Dictionary.sortTags(tags, tagsValue);
            for(String nt : tags){
                if(DEBUG_MODE) Output.print(nt + ": " + tagsValue.get(nt),"red");
            }

            if (chatbotAnswer5(NewTags, answer) < 0) break;
        }
        bot.deleteUser(telegramChat.getMessage().getChatId().toString());
        Output.print(username + " disconnected.", "red");
    }

    private Integer getIncrementValue(String nt) {
        return Math.toIntExact(Math.round((Math.log(tags.indexOf(nt)) / Math.log(LOG_BASE)) * 10));
    }

    private int chatbotAnswer5(LinkedList<String> newTags, String answer) {
        if(!newTags.isEmpty()){
            if(Dictionary.isQuestion(answer)){
                quote(newTags);
            } else {
                quote(newTags);
                int alreadyAsked = justifyAnswer(PROB_CONV, newTags.get(0));
                if(alreadyAsked == 0){
                    askOpinion();
                }
                answer = this.waitAnswer();
                Output.print(username + " - " + answer, "white");
                feelingCount++;
                if(answer.equals("exit")){
                    response("Ha sido un placer hablar contigo! Recuerda que siempre puedes retomar la conversación escribiendo \"/start\".");
                    return -1;
                }
                //Obtener los tags que conoce Sophy
                LinkedList<String> NewTags = Dictionary.getKnownTags(answer);
                for(String nt : NewTags){
                    if(DEBUG_MODE) Output.print(nt,"yellow");
                    if(!tags.contains(nt)){
                        tags.add(nt);
                        tagsValue.put(nt, Dictionary.getKeywordWeight().get(nt));
                    } else {
                        tagsValue.replace(nt, tagsValue.get(nt), tagsValue.get(nt)+VALUE_INCREMENT);
                    }
                }
                tags = Dictionary.sortTags(tags, tagsValue);
                for(String nt : tags){
                    if(DEBUG_MODE) Output.print(nt + ": " + tagsValue.get(nt),"red");
                }
                quote(newTags);
                if(feeling()){
                    //finalRecommendation(tags);
                    finalRecommendation2(tags, tagsValue);
                    return -1;
                } else {
                    try{
                        askFor(tags.get(1));
                    } catch (IndexOutOfBoundsException e){
                        askFor(tags.get(0));
                    }
                }
            }
        } else {
            iKnowNothing(answer);
        }
        return 1;
    }

    private void finalRecommendation2(LinkedList<String> tags, HashMap<String, Integer> tagsValue) {
        int totalPoints = 0;
        for (int i : tagsValue.values()) {
            totalPoints += i;
        }
        LinkedList<Book> books = Dictionary.getBooks();
        LinkedList<Integer> booksValues = new LinkedList<>();
        HashMap<String,Integer> booksValuesHM = new HashMap<>();
        for(Book book : books){
            int bookPoints = 0;
            for(String tag : book.getTags()){
                bookPoints += ((tagsValue.get(tag) != null) ? tagsValue.get(tag) : 0);
            }
            booksValues.add(bookPoints);
            booksValuesHM.put(book.getTitle(),bookPoints);
        }
        Book[] array = books.toArray(new Book[books.size()]);
        Utils.doubleBubbleSort(array,booksValuesHM);
        String temas = "";
        for(String tema : array[0].getTags()){
            temas = temas.concat(tema + ", ");
        }
        temas = temas.substring(0,temas.length()-2);

        response("He encontrado un libro que puede interesarte segun lo que hemos estado hablando. He notado que ahora mismo te puede interesar leer sobre " +
                temas + "... Por ello te recomiendo \"" + array[0].getTitle() + "\", de " + array[0].getAuthor() + ".");

    }

    private int justifyAnswer(int prob, String s) {
        if(s.equals("amor")||s.equals("ateismo")||s.equals("bien")||s.equals("odio")){
            s = "el " + s;
        } else {
            s = "la " + s;
        }
        String finalS = s;
        HashMap<Integer, String> possibleSentences = new HashMap<>(){{
            put(0, "Por que crees eso sobre " + finalS + "?");
            put(1, "Que te lleva a pensar eso? " + finalS + " es un concepto interesante...");
            put(2, "Estoy de acuerdo en cuanto a " + finalS + ", desarrolla un poco mas...");
            put(3, finalS + ", concepto curioso. ¿Qué opinas de este tema?");
            put(4, "Lo cierto es que hablar de " + finalS + " siempre es interesante. ¿Qué más añadirías?");
            put(5, "" + finalS + " me parece un concepto muy amplio, háblame un poco más!");
            put(6, "Me apetece hablar más de " + finalS + "...");
            put(7, "Me gusta mucho tu opinión sobre " + finalS + "! Háblame más de ello...");
            put(8, "Veo que estás hablando de " + finalS + ", vayamos por ahí.");
            put(9, "Poca gente piensa lo mismo que tú sobre " + finalS + "... Háblame más");
            put(10, "No estoy muy segura de que Elisabet piense lo mismo acerca de " + finalS + "; esperemos que no lo lea...");
            put(11, "No estoy muy segura de que Alejandro piense lo mismo acerca de " + finalS + "; esperemos que no lo lea...");
            put(12, "No estoy muy segura de que Ferran piense lo mismo acerca de " + finalS + "; esperemos que no lo lea...");
            put(13, "No estoy muy segura de que Alvaro piense lo mismo acerca de " + finalS + "; esperemos que no lo lea...");
            put(14, "Cada día me fascina más "+ finalS + "... ¿Qué piensas?");
        }};
        if(Rand.getRand(0,10) <= (prob/10)){
            response(possibleSentences.get(Rand.getRand(0,possibleSentences.entrySet().size()-1)));
            return 1;
        }
        return 0;
    }

    private void chatbotAnswer4(LinkedList<String> newTags, String answer) {
        if(!newTags.isEmpty()){
            quote(newTags);
            if(feeling()){
                //finalRecommendation(tags);
            } else {
                askOpinion();
            }
        } else {
            iKnowNothing(answer);
        }
    }

    private void chatbotAnswer3(LinkedList<String> newTags, String answer) {
        //Hay algun new tag?
        if(!newTags.isEmpty()){
            quoteThis(newTags.getFirst());
            if(feeling()){
                //finalRecommendation(tags);
            } else {
                try{
                    quoteThis(newTags.get(1));
                } catch (IndexOutOfBoundsException e){
                    //quoteThis(newTags.getFirst());
                }
                askOpinion();
            }
        } else {
            //Hay algun tag
            if(!tags.isEmpty()){
                quoteThis(tags.getFirst());
                askOpinion();
            } else {
                iKnowNothing(answer);
            }
        }
    }

    private void askOpinion() {
        //TODO: add questions
        response("Que piensas de esto?");
    }

    private void chatbotAnswer2(LinkedList<String> newTags, String answer) {
        //Hay algun tag?
        if(!newTags.isEmpty()) {
            //Pregunta?
            if(Dictionary.isQuestion(answer)){
                //Quote con tag + importante
                quoteThis(newTags.getFirst());
            }
            if(feeling()){
                //finalRecommendation(newTags);
            } else {
                askFor(newTags.getLast());
            }
        } else {
            response("no entiendo nada!");
        }
    }

    private void finalRecommendation(LinkedList<String> newTags) {
        String libroRecomendado = ConnectorDB.getRecommendation(newTags);
        if(libroRecomendado == null) response("No he encontrado ningun libro que pueda ayudarte :(");
        else{

            String[] tok = libroRecomendado.split("/");
            libroRecomendado = tok[0];
            int limit = Integer.parseInt(tok[1]);
            LinkedList<String> finalTags = new LinkedList<>(newTags.subList(0,limit));
            String temas = "";
            for(String tema : finalTags){
                temas = temas.concat(tema + ", ");
            }
            temas = temas.substring(0,temas.length()-2);

            response("He encontrado un libro que te puede gustar ya que trata temas como " + temas + "... Se llama \"" + libroRecomendado + "\", y es de " + ConnectorDB.getAuthor(libroRecomendado) + ".");
        }
    }

    private void quoteThis(String first) {
        response("Estoy haciendo una quote con \"" + first + "\"");
    }

    private void chatbotAnswer(LinkedList<String> NewTags, String answer) {
        //Comprobar si es una pregunta
        if(Dictionary.isQuestion(answer)){
            //Si Sophy no entiende nada de la frase
            if(NewTags.isEmpty()){
                //Coge una palabra random de mas de 4 letras y pregunta por ella
                //Primero comprueba que haya alguna
                iKnowNothing(answer);
            } else {
                if(Chatbot.noFeelingYet()){
                    response("Me has preguntado, pero no hay feeling.");
                    askFor(NewTags.get(0));
                } else {
                    response("Me has preguntado y hay feeling para darte una quote");
                    quote(tags);
                    feelingCount = 0;
                }
            }
        } else {
            if(NewTags.isEmpty()){
                iKnowNothing(answer);
            } else {
                String emotion = Dictionary.getEmotion(NewTags);
                if (emotion != null) {
                    //guardar como tag con mas peso
                    if (Chatbot.noFeelingYet()) {
                        response("Me has hablado de una emocion y no hay feeling para darte una quote");
                        askFor(emotion);
                    } else {
                        response("Me has hablado de una emocion y hay feeling para darte una quote");
                        quote(tags);
                        feelingCount = 0;
                    }
                } else {
                    response("Me has hablado de algo que no es una emocion ni una pregunta");
                    askFor(NewTags.get(0));
                }
            }
        }
    }

    private void iKnowNothing(String answer) {
        String[] splittedAnswer = answer.split(" ");
        int count = 0;
        for(String s : splittedAnswer){
            if(s.length() > 4){
                count++;
            }
        }
        if(count == 0){
            response("Solo se que no se nada...");
            return;
        }
        String randWord = "";
        while (true) {
            int n = Rand.getRand(0, answer.split(" ").length);
            randWord = answer.split(" ")[n];
            if (randWord.length() > 4) break;
        }
        askFor(randWord);
        return;
    }

    private boolean feeling(){
        return (feelingCount > MIN_FEELING && tags.size() > MIN_TAGS);
    }

    private static boolean noFeelingYet() {
        return (feelingCount < MIN_FEELING);
    }

    private void quote(LinkedList<String> newTags) {
        String sentence = null;
        for(int i = newTags.size(); i >= 0; i--){
            sentence = Dictionary.getSentence(newTags.subList(0,i));
            if(usedQuotes.contains(sentence)) continue;
            if(sentence != null) break;
        }
        response(sentence);
        usedQuotes.add(sentence);
    }

    private void askFor(String s) {
        String[] r = {"Que me puedes decir de \"" + s + "\"?",
                    "Que opinion te merece la palabra \"" + s + "\"",
                    "Me quieres hablar un poco mas de \"" + s + "\"?"};
        response(r[Rand.getRand(0,r.length)]);
    }

    private String waitAnswer() {
        while(waiting){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        waiting = true;
        return this.telegramChat.getMessage().getText();
    }

    public void setWaiting(boolean waiting, Update update) {
        this.telegramChat = update;
        this.waiting = waiting;
    }

    @Override
    public void run() {
        startBot();
    }

    void response(String r){
        SendMessage response = new SendMessage();
        response.setChatId(telegramChat.getMessage().getChatId().toString());
        response.setText(r);

        bot.executeE(response);
    }
}
