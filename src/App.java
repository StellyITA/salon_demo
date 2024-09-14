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
                String chosenServiceID = scannerObject.nextLine();
                String serviceNameQuery = "SELECT name FROM services WHERE service_id = " + chosenServiceID;

                try (ResultSet chosenServiceResultSet = statement.executeQuery(serviceNameQuery))
                {
                    chosenServiceResultSet.next();
                    chosenService = chosenServiceResultSet.getString("name");
                    System.out.println(chosenService);

                } catch (Exception e) {
                    System.out.println("Invalid Service number.");
                    continue;
                }

                System.out.println("\nWhat's your phone number?");
                String phone = scannerObject.nextLine();
                String customerName = "";
                String selectCustomer = "SELECT name FROM customers WHERE phone = " + phone;

                try (ResultSet customerNameResultSet = statement.executeQuery(selectCustomer)) {
                    
                    customerNameResultSet.next();
                    customerName = customerNameResultSet.getString("name");
                    System.out.println("Welcome back, " + customerName);

                } catch (SQLException e) {
                    System.out.println("\nWhat's your name?");
                    customerName = scannerObject.nextLine();
                    String addCustomer = "INSERT INTO customers (phone,name) VALUES ('" + phone + "', '" + customerName + "')";
                    statement.executeUpdate(addCustomer);
                }

                break;

            } catch (SQLException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
