package database;

import utils.Rand;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;

public class ConnectorDB {
    private static Connection con;
    private static Statement stmt;
    private static Statement stmt2;

    public static void connect (){
        try{
            con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sophy","root","root");

            stmt = con.createStatement();
            stmt2 = con.createStatement();

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void closeCon(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void executeQuery(String query) {
        try {
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                String libro = rs.getString(2);
                System.out.println(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getRecommendation(LinkedList<String> newTags) {
        LinkedList<String> recommendedBooks = new LinkedList<>();
        int i;
        outer:
        for(i = newTags.size(); i > 0; i--){
            String query = "select * from Libros";
            try {
                ResultSet rs = stmt.executeQuery(query);
                boolean exit = false;
                while(rs.next()){
                    String libroID = rs.getString(1);
                    String libroName = rs.getString(2);
                    query = "select tags.tagNAme from Book_tags, tags where idBook = " + libroID + " and tags.idTag = book_tags.idTag;";
                    ResultSet rs2 = stmt2.executeQuery(query);
                    LinkedList<String> bookTags = new LinkedList<>();
                    while(rs2.next()){
                        bookTags.add(rs2.getString(1).toLowerCase());
                    }
                    if(bookTags.containsAll(newTags.subList(0,i))){
                        recommendedBooks.add(libroName);
                        exit = true;
                    }
                }
                if (exit) break outer;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (!recommendedBooks.isEmpty()) ? recommendedBooks.get(Rand.getRand(0,recommendedBooks.size())).concat("/"+i) : null;

    }

    public static String getAuthor(String libroRecomendado) {
        try {
            ResultSet rs = stmt.executeQuery("select a.nombre from autores a, libros l where l.nombreLibro like \"" + libroRecomendado + "\" and l.idAutor = a.id\n");
            while(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
