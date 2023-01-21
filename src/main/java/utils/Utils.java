package utils;

import database.Dictionary;
import model.Book;
import model.Frase;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Utils {
    
    public static double similarity(String s1, String s2) {
        String longer;
        String shorter;
        int numberOfSimilarities = 0;

        if(s2.length() < s1.length()){
            longer = s1;
            shorter = s2;
        }else{
            shorter = s1;
            longer = s2;
        }

        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        for(int i=0; i < shorter.length(); i++){
            for(int j=0; j< longerLength;j++){
                if(shorter.charAt(i) == longer.charAt(j)){
                    numberOfSimilarities++;
                }
            }
        }
        return (double) numberOfSimilarities / longerLength;
    }
    public static String correctSentence(String answer){
        StringBuffer answerAux = new StringBuffer();
        String finalanswer = null;
        double porcentaje = 0;
        String topics[] = {"amor","ateismo","bien","mal","ciencia","etica","felicidad","guerra","justicia","metafisica","muerte","odio","politica","razon","religion","tristeza","verdad","vida"};
        String[] words = answer.split("\\W+");
        for(int i=0; i < words.length; i++){
            for(int j=0; j < topics.length; j++){
                porcentaje = similarity(words[i],topics[j]);
                if(porcentaje >= 0.7){
                    words[i] = topics[j];
                }
            }
            answerAux.append(words[i].toString()+" ");
        }
        finalanswer = answerAux.toString();
        return finalanswer;
    }
    public static HashMap<String,String> parseDoc(String docName) {
        HashMap<String,String> toReturn = new HashMap<>();
        Scanner sc = null;
        try {
            String f = new File("").getAbsolutePath();
            File file = new File(f.concat("\\src\\main\\java\\dictionaryAssets\\") + docName); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                line = line.toLowerCase();
                if(toReturn.get(line) != null) continue;
                toReturn.put(line,"1");
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
        return toReturn;
    }

    public static LinkedList<Frase> parseSentence(String docName) {
        LinkedList<Frase> toReturn = new LinkedList<>();
        Scanner sc = null;
        try {
            String f = new File("").getAbsolutePath();
            File file = new File(f.concat("\\src\\main\\java\\dictionaryAssets\\quotes\\") + docName); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                LinkedList<String> tags = Dictionary.getKnownTags(line.toLowerCase());
                toReturn.add(new Frase(line,tags));
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
        return toReturn;
    }

    public static void bubbleSort(String[] arr, HashMap<String, Integer> values) {
        int n = arr.length;
        String temp;
        for(int i=0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (values.get(arr[j - 1]) < values.get(arr[j])) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    public static LinkedList<Book> getBooks() {
        LinkedList<Book> books = new LinkedList<>();
        Scanner sc = null;
        try {
            String f = new File("").getAbsolutePath();
            File file = new File(f.concat("\\src\\main\\java\\dictionaryAssets\\") + "booksDataset.txt"); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] parts = line.split("/");
                books.add(new Book(parts[0],parts[1], new LinkedList<String>(Arrays.asList(parts).subList(2,parts.length))));
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
        return books;
    }

    public static void doubleBubbleSort(Book[] arr, HashMap<String, Integer> booksValues) {
        int n = arr.length;
        Book temp;
        for(int i=0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (booksValues.get(arr[j - 1].getTitle()) < booksValues.get(arr[j].getTitle())) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }


}
