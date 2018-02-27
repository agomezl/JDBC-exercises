import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.*;

public class ShipsBattles
{
    /* TODO Here you should put your database name, username and password */
    static final String USERNAME = "tda357";
    static final String PASSWORD = "secret";

    /* Print command usage. */
    public static void usage () {
        System.out.println("Usage: Select a letter to test the corresponding exercise:");
        System.out.println("    (a) Find class with largest firepower");
        System.out.println("    (b) Find countries of ships in a battle");
        System.out.println("    (c) Insert new ships");
        // System.out.println("    (d) Search for errors");
        System.out.println("    (q) Quit");
    }

    /* main: parses the input commands. */
    public static void main(String[] args) throws Exception
    {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/ships_n_battles";
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
                System.out.print("? > ");
                String exercise = console.readLine();
                if (exercise.equalsIgnoreCase("a")) {
                    exerciseA(conn);
                }
                else if (exercise.equalsIgnoreCase("b")) {
                    System.out.print("Enter the name of a battle: ");
                    String battle = console.readLine();
                    exerciseB(conn, battle);
                }
                else if (exercise.equalsIgnoreCase("c")) {
                    Map<String, String> shipClass = new HashMap<String, String>();
                    List<Map<String, String>> ships = new ArrayList<Map<String, String>>();
                    System.out.println("Adding class...");
                    shipClass.put("class", console.readLine("Class name: "));
                    shipClass.put("type", console.readLine("Class type: "));
                    shipClass.put("country", console.readLine("Country: "));
                    shipClass.put("numGuns", console.readLine("Number of guns: "));
                    shipClass.put("bore", console.readLine("Bore: "));
                    shipClass.put("displacement", console.readLine("Displacement: "));
                    System.out.println("Adding ships...");
                    Map<String, String> ship = new HashMap<String, String>();
                    while (true) {
                        String answer = console.readLine("Do you whish to add a ship? [y/n]: ");
                        if (answer.startsWith("n"))
                            break;
                        else if (answer.startsWith("y")) {
                            ship.put("name", console.readLine("Name: "));
                            ship.put("launched", console.readLine("Launch year: "));
                            ships.add(ship);
                        }
                    }
                    exerciseC(conn, shipClass, ships);
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

    private static void exerciseA(Connection conn) throws SQLException {
        String[] attrs = {"class"};

        String query = "WITH R1 AS " +
            "(SELECT CLASS, " +
            "    (numGuns * bore ^ 3) AS firepower " +
            "FROM classes), " +
            "R2 AS " +
            "(SELECT min(firepower) minimum " +
            "FROM R1) " +
            "SELECT CLASS " +
            "FROM R1 " +
            "WHERE firepower = " +
            "    (SELECT minimum " +
            "     FROM R2)";

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        printTable(attrs, rs);
        rs.close();
        st.close();
    }

    private static void exerciseB(Connection conn, String battle) throws SQLException {
        if (countriesForBattle(conn, battle))
            printStatistics(conn, battle);
    }

    private static boolean countriesForBattle(Connection conn, String battle) throws SQLException {
        String[] attrs = {"countries"};
        String query = "WITH R1 AS " +
            "(SELECT ship AS name " +
            "FROM outcomes " +
            "WHERE battle = ?), " +
            "R2 AS " +
            "(SELECT DISTINCT CLASS " +
            "FROM ships " +
            "    NATURAL JOIN R1) " +
            "SELECT country " +
            "FROM classes " +
            "NATURAL JOIN R2";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, battle);
        ResultSet rs = st.executeQuery();
        boolean foundResult = rs.isBeforeFirst();
        if (foundResult) {
            System.out.println("Participating countries");
            System.out.println();
            printTable(attrs, rs);
            System.out.println();
        } else
            System.out.println("There is no information on this battle.");
        rs.close();
        st.close();
        return foundResult;
    }

    private static void printStatistics(Connection conn, String battle) throws SQLException {
        String[] attrs = {"countries"};
        String query = "WITH R1 AS " +
            "(SELECT CLASS, " +
            "    name AS ship, " +
            "    country " +
            "FROM classes " +
            "    NATURAL JOIN ships), " +
            "R2 AS " +
            "(SELECT battle, " +
            "    RESULT, " +
            "    country " +
            "FROM outcomes " +
            "    NATURAL JOIN R1), " +
            "R3 AS " +
            "(SELECT count(*) AS COUNT, " +
            "    country " +
            "FROM R2 " +
            "WHERE RESULT = ? " +
            "    AND battle = ? " +
            "GROUP BY country) " +
            "SELECT country " +
            "FROM R3 " +
            "WHERE COUNT = " +
            "    (SELECT max(COUNT) " +
            "     FROM R3)";
        PreparedStatement st = conn.prepareStatement(query);
        for (String status : new String[] {"sunk", "damaged"}) {
            st.setString(1, status);
            st.setString(2, battle);
            ResultSet rs = st.executeQuery();
            if (!rs.isBeforeFirst())
                System.out.println("No ships " +
                                   status +
                                   " during the battle of " +
                                   battle);
            else {
                System.out.println("Countries with the most ships " +
                                   status +
                                   " on the battle of " +
                                   battle);
                System.out.println();
                printTable(attrs, rs);
            }
            System.out.println();
            rs.close();
        }
        st.close();
    }

    private static void exerciseC(Connection conn,
                                  Map<String, String> shipClass,
                                  List<Map<String, String>> ships) throws SQLException {
        try {
            conn.setAutoCommit(false);
            String query = "INSERT INTO Classes VALUES (?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, shipClass.get("class"));
            st.setString(2, shipClass.get("type"));
            st.setString(3, shipClass.get("country"));
            st.setInt(4, Integer.parseInt(shipClass.get("numGuns")));
            st.setInt(5, Integer.parseInt(shipClass.get("bore")));
            st.setInt(6, Integer.parseInt(shipClass.get("displacement")));
            st.executeUpdate();
            st.close();
            query = "INSERT INTO Ships VALUES (?,?,?)";
            st = conn.prepareStatement(query);
            for (Map<String,String> ship : ships) {
                st.setString(1, ship.get("name"));
                st.setString(2, shipClass.get("class"));
                st.setInt(3, Integer.parseInt(ship.get("launched")));
                st.executeUpdate();
            }
            conn.commit();
            System.out.println("The data has been succesfully added.");
        } catch (Exception e) {
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
