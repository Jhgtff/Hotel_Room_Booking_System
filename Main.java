import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        Connection con = DatabaseConnection.getInstance().getConnection();
        if (con == null) {
            System.out.println("Database connection failed.");
            return;
        }
        String sql = "SELECT room_number, room_type, price FROM rooms ORDER BY room_number";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Rooms stored in database:");
            while (rs.next()) {
                System.out.println("Room Number: " + rs.getInt("room_number"));
                System.out.println("Room Type: " + rs.getString("room_type"));
                System.out.println("Price: Rs. " + rs.getDouble("price"));
                System.out.println("-------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
