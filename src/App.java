import java.sql.*;
import java.util.Scanner;

public class App {
    static final String DB_URL = "jdbc:mysql://localhost:3306/salon";
    static final String USER = "stelly";
    static final String PASSWORD = "examplepassword";
    static final String RETRIEVE_SERVICES = "SELECT * FROM services";
    public static void main(String[] args) throws Exception {

        System.out.println("\n~~~ Welcome to SalonName appointment scheduler! ~~~\n");

        while (true) {
            System.out.println("\nWhich service would you like to book?\n");

            try (Connection dbConnection = DriverManager.getConnection(DB_URL,USER,PASSWORD);
            Statement statement = dbConnection.createStatement();
            ResultSet services = statement.executeQuery(RETRIEVE_SERVICES);) {
                
                while (services.next())
                {
                    System.out.print(services.getString("service_id") + ") ");
                    System.out.println(services.getString("name"));
                }

                String chosenService;
                Scanner scannerObject = new Scanner(System.in);
                String serviceNameQuery = "SELECT name FROM services WHERE service_id = " + scannerObject.nextLine();
                
                try (ResultSet chosenServiceResultSet = statement.executeQuery(serviceNameQuery))
                {
                    chosenServiceResultSet.next();
                    chosenService = chosenServiceResultSet.getString("name");
                    System.out.println(chosenService);

                } catch (SQLSyntaxErrorException e) {
                    System.out.println("Invalid Service number.");
                    continue;
                }
                
                // TODO: Book appointment

                scannerObject.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
