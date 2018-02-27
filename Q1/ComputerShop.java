import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.*;

public class ComputerShop
{
    /* TODO Here you should put your database name, username and password */
    static final String USERNAME = "tda357";
    static final String PASSWORD = "secret";

    /* Print command usage. */
    public static void usage () {
        System.out.println("Usage: Select a letter to test the corresponding exercise:");
        System.out.println("    (a) Find the PC whose price is closest to the one desired");
        System.out.println("    (b) Find laptop that satisfies user requirements");
        System.out.println("    (c) Print spec of all products by manufacturer");
        System.out.println("    (d) Find the cheapest \"system\" (PC + printer)");
        System.out.println("    (e) Check there is no PC with specified model number");
        System.out.println("    (q) Quit");
    }

    /* main: parses the input commands. */
    public static void main(String[] args) throws Exception
    {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/computer-shop";
            Properties props = new Properties();
            props.setProperty("user",USERNAME);
            props.setProperty("password",PASSWORD);
            Connection conn = DriverManager.getConnection(url, props);

            Console console = System.console();
            // In Eclipse. System.console() returns null due to a bug (https://bugs.eclipse.org/bugs/show_bug.cgi?id=122429)
            // In that case, use the following line instead:
            // BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            usage();
            System.out.println("Welcome!");
            while(true) {
                String exercise = console.readLine("? > ");
                if (exercise.equalsIgnoreCase("a")) {
                    int price = Integer.parseInt(console.readLine("Enter a price: "));
                    exerciseA(conn, price);
                }
                else if (exercise.equalsIgnoreCase("b")) {
                    float speed = Float.parseFloat(console.readLine("Enter minimum speed: "));
                    float ram = Float.parseFloat(console.readLine("Enter minimum RAM: "));
                    float hd = Float.parseFloat(console.readLine("Enter minimum hard-disk size: "));
                    float screen = Float.parseFloat(console.readLine("Enter minimum screen size: "));
                    exerciseB(conn, speed, ram, hd, screen);
                }
                else if (exercise.equalsIgnoreCase("c")) {
                    String manufacturer = console.readLine("Enter a manufacturer: ");
                    exerciseC(conn, manufacturer);
                }
                else if (exercise.equalsIgnoreCase("d")) {
                    int budget = Integer.parseInt(console.readLine("Enter the budget (PC price + printer price): "));
                    float speed = Float.parseFloat(console.readLine("Enter the minimum speed: "));
                    exerciseD(conn, budget, speed);
                }
                else if (exercise.equalsIgnoreCase("e")) {
                    String manufacturer = console.readLine("Enter the manufacturer: ");
                    int model = Integer.parseInt(console.readLine("Enter the model: "));
                    float speed = Float.parseFloat(console.readLine("Enter the speed: "));
                    float ram = Float.parseFloat(console.readLine("Enter the RAM: "));
                    float hd = Float.parseFloat(console.readLine("Enter the hard-disk size: "));
                    float price = Float.parseFloat(console.readLine("Enter the price: "));
                    exerciseE(conn,
                              manufacturer,
                              model,
                              speed,
                              ram,
                              hd,
                              price);
                }
                else if (exercise.equalsIgnoreCase("q")) {
                               break;
                } else
                      usage();
            }
            System.out.println("Goodbye!");
            conn.close();
        } catch (SQLException e) {
            System.err.println(e);
            System.exit(2);
        }
    }

    private static void printTable(String[] attributeNames, ResultSet rs) throws SQLException {
        List<String> placeholders = Collections.nCopies(attributeNames.length, "%8s");
        String format = String.join(" | ", placeholders);
        String header = String.format(format, (Object[]) attributeNames);
        String border = String.join("-+-",
                                    Collections.nCopies(attributeNames.length, "--------"));
        System.out.println(header);
        System.out.println(border);

        while (rs.next()) {
            List<String> values = new ArrayList<String>();
            for (int i = 1; i <= attributeNames.length; i++)
                values.add(rs.getString(i));
            String row = String.format(format, values.toArray(new Object[values.size()]));
            System.out.println(row);
        }
    }

    private static void exerciseA(Connection conn, int price) throws SQLException {
        String query = "SELECT * FROM Pcs";
        PreparedStatement st = conn.prepareStatement(query);
        // st.setInt(1, price);
        ResultSet rs = st.executeQuery();
        printTable(new String[]{"model", "speed", "ram", "hd", "price"},rs);
        rs.close();
        st.close();
    }

    private static void exerciseB(Connection conn,
                                  float speed,
                                  float ram,
                                  float hd,
                                  float screen) throws SQLException {
        String[] attrs = {"maker", "speed", "ram", "hd", "screen"};
        String query = "SELECT * FROM Laptop";
        PreparedStatement st = conn.prepareStatement(query);
        // st.setFloat(1, speed);
        // st.setFloat(2, ram);
        // st.setFloat(3, hd);
        // st.setFloat(4, screen);
        ResultSet rs = st.executeQuery();
        printTable(attrs, rs);
        rs.close();
        st.close();
    }

    private static void exerciseC(Connection conn, String manufacturer) throws SQLException {
        String query = "SELECT * FROM Products";
        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        printTable(new String[]{"maker","model","type"},rs);
        rs.close();
        st.close();
    }

    private static void exerciseD(Connection conn, int budget, float speed) throws SQLException {
        String query = "SELECT * FROM Pcs";
        PreparedStatement st = conn.prepareStatement(query);
        // st.setInt(1, price);
        ResultSet rs = st.executeQuery();
        printTable(new String[]{"model", "speed", "ram", "hd", "price"},rs);
        rs.close();
        st.close();
    }

    private static void exerciseE(Connection conn,
                                  String maker,
                                  int model,
                                  float speed,
                                  float ram,
                                  float hd,
                                  float price) throws SQLException {
        String query = "SELECT * FROM Products";
        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        printTable(new String[]{"maker","model","type"},rs);
        rs.close();
        st.close();
    }
}
