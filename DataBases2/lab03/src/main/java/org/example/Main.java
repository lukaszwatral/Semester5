package org.example;
import java.sql.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lab3", "root", "");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS tabela(id INT PRIMARY KEY AUTO_INCREMENT, liczba INT, tekst VARCHAR(255))");
            stmt.executeUpdate("DELETE FROM tabela");
            stmt.executeUpdate("INSERT INTO tabela(liczba, tekst) VALUES (2, 'Polska')");
            stmt.executeUpdate("INSERT INTO tabela(liczba, tekst) VALUES (14, 'Niemcy')");
            stmt.executeUpdate("INSERT INTO tabela(liczba, tekst) VALUES (5, 'Wlochy')");


            String[] tab = {"Francja", "Hiszpania", "Portugalia", "Chorwacja", "Grecja", "Dania", "Norwegia", "Turcja", "Slowenia", "Kosowo", "Luksemburg"};
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tabela (liczba, tekst) VALUES (?, ?)");
            for (String s : tab) {
                ps.setInt(1, new Random().nextInt());
                ps.setString(2, s);
                ps.executeUpdate();
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM tabela");
            while(rs.next()){
                System.out.println(rs.getInt("id") + ". " + rs.getString("tekst") + " - " + rs.getInt("liczba"));
            }

        } catch(SQLException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
