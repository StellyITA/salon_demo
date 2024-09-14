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
                String customerID = "";
                String selectCustomer = "SELECT name, customer_id FROM customers WHERE phone = " + phone;

                try (ResultSet customerNameResultSet = statement.executeQuery(selectCustomer)) {
                    
                    customerNameResultSet.next();
                    customerName = customerNameResultSet.getString("name");
                    customerID = customerNameResultSet.getString("customer_id");
                    System.out.println("Welcome back, " + customerName);

                } catch (SQLException e) {
                    System.out.println("\nWhat's your name?");
                    customerName = scannerObject.nextLine();
                    String addCustomer = "INSERT INTO customers (phone,name) VALUES ('" + phone + "', '" + customerName + "')";
                    statement.executeUpdate(addCustomer);
                    ResultSet customerNameResultSet = statement.executeQuery(selectCustomer);
                    customerNameResultSet.next();
                    customerID = customerNameResultSet.getString("customer_id");
                }

                System.out.println("\nSelect a time for the service you would like to book (format hh:mm)");
                String time = scannerObject.nextLine();
                String addAppointment = "INSERT INTO appointments (customer_id, service_id, time) VALUES ('" + customerID + "', '" + chosenServiceID + "', '" + time + "')";
                
                try {
                    statement.executeUpdate(addAppointment);
                } catch (SQLException e) {
                    System.out.println("Wrong time format.");
                    continue;
                }

                System.out.println("\nI have put you down for a " + chosenService + " at " + time + ", " + customerName);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            break;
        }

    }
}
