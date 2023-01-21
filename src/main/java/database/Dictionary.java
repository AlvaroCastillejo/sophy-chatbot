package database;

import model.Book;
import model.Frase;
import utils.Rand;
import utils.Utils;

import java.util.*;

public class Dictionary {

    private static final LinkedList<String> keywords = new LinkedList<>(List.of("bien","mal","verdad","muerte","vida","amor","ciencia","paz","guerra","razon","ateismo","etica","religion","felicidad","odio","mentira","tristeza","justicia","metafisica","politica"));
    private static final HashMap<String, Integer> keywordWeight = new HashMap<>(){{
        put("bien",9);
        put("mal",9);
        put("verdad",9);
        put("muerte",8);
        put("vida",8);
        put("amor",8);
        put("ciencia",8);
        put("paz",8);
        put("guerra",8);
        put("razon",8);
        put("ateismo",8);
        put("etica",7);
        put("religion",7);
        put("felicidad",6);
        put("odio",6);
        put("mentira",6);
        put("tristeza",5);
        put("justicia",4);
        put("metafisica",3);
        put("politica",3);
        /*put("muerte",9);
        put("guerra",9);
        put("odio",9);
        put("razon",8);
        put("justicia",8);
        put("etica",8);
        put("amor",8);
        put("verdad",8);
        put("paz",7);
        put("politica",7);
        put("religion",6);
        put("ateismo",6);
        put("tristeza",5);
        put("felicidad",5);
        put("bien",4);
        put("mal",4);
        put("mentira",4);
        put("vida",3);
        put("metafisica",2);
        put("ciencia",2);*/
    }};



    private static final LinkedList<String> emotions = new LinkedList<>(List.of("felicidad", "odio", "tristeza"));
    private static HashMap<String,String> muerteSinon = new HashMap<>();
    private static HashMap<String,String> tristezaSinon = new HashMap<>();
    private static HashMap<String,String> felicidadSinon = new HashMap<>();
    private static HashMap<String,String> vidaSinon = new HashMap<>();
    private static HashMap<String,String> eticaSinon = new HashMap<>();
    private static HashMap<String,String> justiciaSinon = new HashMap<>();
    private static HashMap<String,String> amorSinon = new HashMap<>();
    private static HashMap<String,String> odioSinon = new HashMap<>();
    private static HashMap<String,String> verdadSinon = new HashMap<>();
    private static HashMap<String,String> mentiraSinon = new HashMap<>();
    private static HashMap<String,String> metafisicaSinon = new HashMap<>();
    private static HashMap<String,String> cienciaSinon = new HashMap<>();
    private static HashMap<String,String> pazSinon = new HashMap<>();
    private static HashMap<String,String> guerraSinon = new HashMap<>();
    private static HashMap<String,String> razonSinon = new HashMap<>();
    private static HashMap<String,String> religionSinon = new HashMap<>();
    private static HashMap<String,String> ateismoSinon = new HashMap<>();
    private static HashMap<String,String> bienSinon = new HashMap<>();
    private static HashMap<String,String> malSinon = new HashMap<>();
    private static HashMap<String,String> politicaSinon = new HashMap<>();

    private static HashMap<String,LinkedList<Frase>> frasesHash = new HashMap<>();
    private static LinkedList<Frase> frasesAteismo = new LinkedList<>();
    private static LinkedList<Frase> frasesEtica = new LinkedList<>();
    private static LinkedList<Frase> frasesFelicidad = new LinkedList<>();
    private static LinkedList<Frase> frasesJusticia = new LinkedList<>();
    private static LinkedList<Frase> frasesMentira = new LinkedList<>();
    private static LinkedList<Frase> frasesMetafisica = new LinkedList<>();
    private static LinkedList<Frase> frasesOdio = new LinkedList<>();
    private static LinkedList<Frase> frasesPolitica = new LinkedList<>();
    private static LinkedList<Frase> frasesRazon = new LinkedList<>();
    private static LinkedList<Frase> frasesReligion = new LinkedList<>();
    private static LinkedList<Frase> frasesTristeza = new LinkedList<>();
    private static LinkedList<Frase> frasesVida = new LinkedList<>();
    private static LinkedList<Frase> frasesBien = new LinkedList<>();
    private static LinkedList<Frase> frasesMal = new LinkedList<>();
    private static LinkedList<Frase> frasesVerdad = new LinkedList<>();
    private static LinkedList<Frase> frasesMuerte = new LinkedList<>();
    private static LinkedList<Frase> frasesAmor = new LinkedList<>();
    private static LinkedList<Frase> frasesCiencia = new LinkedList<>();
    private static LinkedList<Frase> frasesPaz = new LinkedList<>();
    private static LinkedList<Frase> frasesGuerra = new LinkedList<>();

    private static LinkedList<Book> books = new LinkedList<>();

    public static boolean isQuestion(String answer) {
        return answer.contains("?");
    }

    public static HashMap<String, Integer> getKeywordWeight(){
        return keywordWeight;
    }

    public static LinkedList<String> getKnownTags(String answer) {
        LinkedList<String> toReturn = new LinkedList<>();
        String[] tokens = answer.split(" ");
        for (String token : tokens){
            token = token.toLowerCase();
            if(token.endsWith("?")) token = token.substring(0,token.length()-1);
            if(token.endsWith(".")) token = token.substring(0,token.length()-1);
            if(token.endsWith(",")) token = token.substring(0,token.length()-1);
            if(token.endsWith(")")) token = token.substring(0,token.length()-1);
            if(token.endsWith("(")) token = token.substring(0,token.length()-1);
            if(token.endsWith("!")) token = token.substring(0,token.length()-1);
            if(token.endsWith(";")) token = token.substring(0,token.length()-1);
            if(keywords.contains(token)){
                toReturn.add(token);
                continue;
            }

            //peso 9
            if(bienSinon.get(token) != null){
                toReturn.add("bien");
            }
            if(malSinon.get(token) != null){
                toReturn.add("mal");
            }
            if(verdadSinon.get(token) != null){
                toReturn.add("verdad");
            }

            //peso 8
            if(muerteSinon.get(token) != null){
                toReturn.add("muerte");
            }
            if(vidaSinon.get(token) != null){
                toReturn.add("vida");
            }
            if(amorSinon.get(token) != null){
                toReturn.add("amor");
            }
            if(cienciaSinon.get(token) != null){
                toReturn.add("ciencia");
            }
            if(pazSinon.get(token) != null){
                toReturn.add("paz");
            }
            if(guerraSinon.get(token) != null){
                toReturn.add("guerra");
            }
            if(razonSinon.get(token) != null){
                toReturn.add("razon");
            }
            if(ateismoSinon.get(token) != null){
                toReturn.add("ateismo");
            }

            //Peso 7
            if(eticaSinon.get(token) != null){
                toReturn.add("etica");
            }
            if(religionSinon.get(token) != null){
                toReturn.add("religion");
            }

            //Peso 6
            if(felicidadSinon.get(token) != null){
                toReturn.add("felicidad");
            }
            if(odioSinon.get(token) != null){
                toReturn.add("odio");
            }
            if(mentiraSinon.get(token) != null){
                toReturn.add("mentira");
            }

            //Peso 5
            if(tristezaSinon.get(token) != null){
                toReturn.add("tristeza");
            }

            //Peso 4
            if(justiciaSinon.get(token) != null){
                toReturn.add("justicia");
            }

            //Peso 3
            if(metafisicaSinon.get(token) != null){
                toReturn.add("metafisica");
            }
            if(politicaSinon.get(token) != null){
                toReturn.add("politica");
            }

            /*if(muerteSinon.get(token) != null){
                toReturn.add("muerte");
            }
            if(guerraSinon.get(token) != null){
                toReturn.add("guerra");
            }
            if(odioSinon.get(token) != null){
                toReturn.add("odio");
            }
            if(razonSinon.get(token) != null){
                toReturn.add("razon");
            }
            if(justiciaSinon.get(token) != null){
                toReturn.add("justicia");
            }
            if(eticaSinon.get(token) != null){
                toReturn.add("etica");
            }
            if(amorSinon.get(token) != null){
                toReturn.add("amor");
            }
            if(verdadSinon.get(token) != null){
                toReturn.add("verdad");
            }
            if(pazSinon.get(token) != null){
                toReturn.add("paz");
            }
            if(politicaSinon.get(token) != null){
                toReturn.add("politica");
            }
            if(religionSinon.get(token) != null){
                toReturn.add("religion");
            }
            if(ateismoSinon.get(token) != null){
                toReturn.add("ateismo");
            }
            if(tristezaSinon.get(token) != null){
                toReturn.add("tristeza");
            }
            if(felicidadSinon.get(token) != null){
                toReturn.add("felicidad");
            }
            if(bienSinon.get(token) != null){
                toReturn.add("bien");
            }
            if(malSinon.get(token) != null){
                toReturn.add("mal");
            }
            if(mentiraSinon.get(token) != null){
                toReturn.add("mentira");
            }
            if(vidaSinon.get(token) != null){
                toReturn.add("vida");
            }
            if(metafisicaSinon.get(token) != null){
                toReturn.add("metafisica");
            }
            if(cienciaSinon.get(token) != null){
                toReturn.add("ciencia");
            }*/


            //peso 8

            //Peso 7

            //Peso 6

            //Peso 5

            //Peso 4

            //Peso 3
        }
        toReturn = Dictionary.sortTags(toReturn, null);
        return toReturn;
    }

    public static LinkedList<String> sortTags(LinkedList<String> toReturn, HashMap<String, Integer> tagsValue) {
        LinkedList<String> sorted = new LinkedList<>();
        if(tagsValue == null){
            for(String tag : keywords){
                if(toReturn.contains(tag)) sorted.add(tag);
            }
            return sorted;
        } else {
            String[] array = toReturn.toArray(new String[toReturn.size()]);
            Utils.bubbleSort(array, tagsValue);
            List<String> list = Arrays.asList(array);
            return new LinkedList<String>(list);
        }
    }

    public static boolean isEmotion(LinkedList<String> newTags) {
        return false;
    }

    public static String getEmotion(LinkedList<String> newTags) {
        for(String token : newTags){
            token = token.toLowerCase();
            return (emotions.contains(token)) ? token : null;
        }
        return null;
    }

    public static void initializeDatabase() {
        muerteSinon = Utils.parseDoc("sinonimos_muerte.txt");
        tristezaSinon = Utils.parseDoc("sinonimos_tristeza.txt");
        felicidadSinon = Utils.parseDoc("sinonimos_felicidad.txt");
        vidaSinon = Utils.parseDoc("sinonimos_vida.txt");
        eticaSinon = Utils.parseDoc("sinonimos_etica.txt");
        justiciaSinon = Utils.parseDoc("sinonimos_justicia.txt");
        amorSinon = Utils.parseDoc("sinonimos_amor.txt");
        odioSinon = Utils.parseDoc("sinonimos_odio.txt");
        verdadSinon = Utils.parseDoc("sinonimos_verdad.txt");
        mentiraSinon = Utils.parseDoc("sinonimos_verdad.txt");
        metafisicaSinon = Utils.parseDoc("sinonimos_metafisica.txt");
        cienciaSinon = Utils.parseDoc("sinonimos_ciencia.txt");
        pazSinon = Utils.parseDoc("sinonimos_guerra.txt");
        guerraSinon = Utils.parseDoc("sinonimos_guerra.txt");
        razonSinon = Utils.parseDoc("sinonimos_razon.txt");
        religionSinon = Utils.parseDoc("sinonimos_religion.txt");
        ateismoSinon = Utils.parseDoc("sinonimos_ateismo.txt");
        bienSinon = Utils.parseDoc("sinonimos_bien.txt");
        malSinon = Utils.parseDoc("sinonimos_bien.txt");
        politicaSinon = Utils.parseDoc("sinonimos_politica.txt");

        frasesAteismo = Utils.parseSentence("ateismo.txt");
        frasesEtica = Utils.parseSentence("etica.txt");
        frasesJusticia = Utils.parseSentence("justicia.txt");
        frasesMentira = Utils.parseSentence("mentira.txt");
        frasesMetafisica = Utils.parseSentence("metafisica.txt");
        frasesOdio = Utils.parseSentence("odio.txt");
        frasesPolitica = Utils.parseSentence("politica.txt");
        frasesRazon = Utils.parseSentence("razon.txt");
        frasesReligion = Utils.parseSentence("religion.txt");
        frasesBien = Utils.parseSentence("bien.txt");
        frasesMal = Utils.parseSentence("mal.txt");
        frasesVerdad = Utils.parseSentence("verdad.txt");
        frasesMuerte = Utils.parseSentence("muerte.txt");
        frasesAmor = Utils.parseSentence("amor.txt");
        frasesCiencia = Utils.parseSentence("ciencia.txt");
        frasesPaz = Utils.parseSentence("paz.txt");
        frasesGuerra = Utils.parseSentence("guerra.txt");
        frasesFelicidad = Utils.parseSentence("felicidad.txt");
        frasesVida = Utils.parseSentence("vida.txt");
        frasesTristeza = Utils.parseSentence("tristeza.txt");


        frasesHash.put("ateismo", frasesAteismo);
        frasesHash.put("etica", frasesEtica);
        frasesHash.put("bien",frasesBien);
        frasesHash.put("mal",frasesMal);
        frasesHash.put("verdad",frasesVerdad);
        frasesHash.put("muerte",frasesMuerte);
        frasesHash.put("amor",frasesAmor);
        frasesHash.put("ciencia",frasesCiencia);
        frasesHash.put("paz",frasesPaz);
        frasesHash.put("guerra",frasesGuerra);
        frasesHash.put("razon",frasesRazon);
        frasesHash.put("religion",frasesReligion);
        frasesHash.put("felicidad",frasesFelicidad);
        frasesHash.put("odio",frasesOdio);
        frasesHash.put("mentira",frasesMentira);
        frasesHash.put("tristeza",frasesTristeza);
        frasesHash.put("justicia",frasesJusticia);
        frasesHash.put("metafisica",frasesMetafisica);
        frasesHash.put("politica",frasesPolitica);
        frasesHash.put("vida",frasesVida);

        books = Utils.getBooks();


    }

    public static LinkedList<Book> getBooks() {
        return books;
    }

    public static String getSentence(List<String> subList) {
        LinkedList<Frase> frases = frasesHash.get(subList.get(0));
        LinkedList<Frase> allFrases = new LinkedList<>();
        for(Frase item : frases){
            //Ignoramos el primero
            int totalToFind = subList.size()-1;
            int totalFound = 0;
            for(int i = 0; i < item.getTags().size(); i++){
                for (int j = 1; j < subList.size(); j++){
                    if(subList.get(j).equals(item.getTags().get(i))){
                        totalFound++;
                    }
                }
            }
            if(totalFound == totalToFind){
                allFrases.add(item);
            }
        }
        return (allFrases.isEmpty()) ? null : allFrases.get(Rand.getRand(0,allFrases.size())).getFrase();
    }
}
