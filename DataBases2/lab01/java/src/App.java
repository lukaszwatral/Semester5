import java.sql.*;

public class App{
public static void main(String[] args) throws SQLException {

    
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/my_db", "root", "");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM osoby");

    
        while(rs.next()) {
            int liczba = rs.getInt("id");
            String imie = rs.getString("imie");
            String nazwisko = rs.getString("nazwisko");

            System.out.println(liczba + ". " + imie + " " + nazwisko);
        }
    }
}
