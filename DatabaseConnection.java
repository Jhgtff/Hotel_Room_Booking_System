import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotel_booking", "root", "");
            System.out.println("Database connected!");
            createTables();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check that MySQL is running and the hotel baoking database exist.");
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(100) NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "phone VARCHAR(15))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rooms ("
                + "room_number INT PRIMARY KEY, "
                + "room_type VARCHAR(50) NOT NULL, "
                + "price DOUBLE NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings ("
                + "booking_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(100) NOT NULL, "
                + "hotel_name VARCHAR(100) NOT NULL, "
                + "room_type VARCHAR(50) NOT NULL, "
                + "price DOUBLE NOT NULL, "
                + "booking_date DATE NOT NULL, "
                + "check_in DATE NOT NULL, "
                + "check_out DATE NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS payments ("
                + "payment_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(100) NOT NULL, "
                + "hotel_name VARCHAR(100) NOT NULL, "
                + "amount DOUBLE NOT NULL, "
                + "payment_method VARCHAR(50) NOT NULL, "
                + "payment_status VARCHAR(50) NOT NULL, "
                + "payment_date DATE NOT NULL)");
        }

    private void addRoom(PreparedStatement ps, int number, String type, double price) throws SQLException {
        ps.setInt(1, number);
        ps.setString(2, type);
        ps.setDouble(3, price);
        ps.executeUpdate();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
