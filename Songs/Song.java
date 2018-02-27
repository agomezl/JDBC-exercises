import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.*;

public class Song {
    //
    static final String USERNAME = "tda357";
    static final String PASSWORD = "secret";
    
    public static void main(String[] args) throws Exception {
        // Get the postgres driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe){
            System.out.println("Could not find the JDBC driver!");
            System.exit(1);
        }
      
        try {
            // Connect to the database
            String url = "jdbc:postgresql://localhost/songs";
            Properties props = new Properties();
            props.setProperty("user",USERNAME);
            props.setProperty("password",PASSWORD);
            Connection conn = DriverManager.getConnection(url, props);
            Console console = System.console();
            
            // Do stuff
            int year;
            
            while (true) {
                try {
                    System.out.println("When where you born?");
                    String exercise = console.readLine("? > ");
                    year = Integer.parseInt(exercise);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please insert a number!");
                }
            }
            
            String query = "SELECT * FROM Song WHERE ? + 1 >= year AND year + 5 >= ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, year);
            stmt.setInt(2, year);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("You song is: " + rs.getString(2));
                System.out.println("Go give it a listen at " + rs.getString(5));
            } else {
                System.out.println("There is no song for you, I'm sorry = (");
            }
            conn.close();
            
           
        } catch (SQLException e) {
            System.err.println(e);
            System.exit(2);
        }
    }



}
