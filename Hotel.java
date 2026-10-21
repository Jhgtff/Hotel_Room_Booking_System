import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

class Hotel {
    Hotel() {
        JFrame f = new JFrame("Select Hotel and Room");
        JLabel title = new JLabel("Select Your Hotel and Room");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(JLabel.CENTER);

        String[][] hotels = {
            {"Hotel Green Park", "Kochi"},
            {"Grand Palace Hotel", "Kottayam"},
            {"Sunrise Residency", "Kochi"}
        };

        JComboBox<String> choice = new JComboBox<>();
        choice.setVisible(false);
        JTextField location = new JTextField(15);
        location.setText("Enter city");
        JButton search = new JButton("Search Location");
        JButton details = new JButton("View Hotel Details");
        JComboBox<String> roomChoice = new JComboBox<>(new String[] {
            "Single Room - Rs. 1000/night",
            "Deluxe Room - Rs. 2000/night",
            "Suite Room - Rs. 3000/night"
        });
        JLabel checkInLabel = new JLabel("Check-in (YYYY-MM-DD):");
        JTextField checkIn = new JTextField(10);
        JLabel checkOutLabel = new JLabel("Check-out (YYYY-MM-DD):");
        JTextField checkOut = new JTextField(10);
        JButton book = new JButton("Book Room");
      JButton backtodash = new JButton("Back To Login");

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.add(new JLabel("Enter city (Kochi or Kottayam):"));
        form.add(location);
        form.add(search);
    form.add(choice);
        form.add(details);
        form.add(new JLabel("Choose Room Type:"));
        form.add(roomChoice);
      form.add(checkInLabel);
        form.add(checkIn);
       form.add(checkOutLabel);
       form.add(checkOut);
        form.add(book);
        form.add(backtodash);
        JPanel center = new JPanel(new GridBagLayout());
        center.add(form);
        f.setLayout(new BorderLayout(10, 15));
        f.add(title, BorderLayout.NORTH);
      f.add(center, BorderLayout.CENTER);

        search.addActionListener(e -> {
            String city = location.getText().trim();
            choice.removeAllItems();
            if (city.isEmpty() || city.equalsIgnoreCase("Enter city")) {
                JOptionPane.showMessageDialog(f, "Please enter a city.");
                choice.setVisible(false);
                return;
            }
            for (String[] hotel : hotels) {
                if (hotel[1].equalsIgnoreCase(city)) choice.addItem(hotel[0]);
            }
          choice.setVisible(choice.getItemCount() > 0);
            form.revalidate();
            form.repaint();
            if (choice.getItemCount() > 0) {
                JOptionPane.showMessageDialog(f, choice.getItemCount() + " hotel(s) found in " + city);
            } else {
                JOptionPane.showMessageDialog(f, "No hotels found in " + city);
            }
        });

        details.addActionListener(e -> {
          String hotel = (String) choice.getSelectedItem();
            if (hotel == null) {
                JOptionPane.showMessageDialog(f, "Please search for a city and select a hotel first.");
                return;
            }
        if (hotel.equals("Hotel Green Park")) {
                JOptionPane.showMessageDialog(f, "Hotel: Hotel Green Park\nType: Budget Hotel\nFacilities: Wi-Fi, Restaurant");
          } else if (hotel.equals("Grand Palace Hotel")) {
                JOptionPane.showMessageDialog(f, "Hotel: Grand Palace Hotel\nType: Luxury Hotel\nFacilities: Wi-Fi, Pool, Restaurant");
            } else {
                JOptionPane.showMessageDialog(f, "Hotel: Sunrise Residency\nType: Family Hotel\nFacilities: Wi-Fi, Parking, Restaurant");
            }
        });

        book.addActionListener(e -> {
            String hotel = (String) choice.getSelectedItem();
            if (hotel == null) {
                JOptionPane.showMessageDialog(f, "Please search for a city and select a hotel first.");
                return;
            }

        Room room;
            int selectedRoom = roomChoice.getSelectedIndex();
            if (selectedRoom == 0) room = new SingleRoom(101, 1000);
        else if (selectedRoom == 1) room = new DeluxeRoom(102, 2000);
        else room = new SuiteRoom(103, 3000);
            room.displayDetails();

            LocalDate in;
            LocalDate out;
            try {
                in = LocalDate.parse(checkIn.getText().trim());
                out = LocalDate.parse(checkOut.getText().trim());
            if (!out.isAfter(in)) {
                    JOptionPane.showMessageDialog(f, "Check-out date must be after check-in date.");
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(f, "Enter dates in YYYY-MM-DD format.");
                return;
            }

            String username = JOptionPane.showInputDialog(f, "Enter your registration username:");
            if (username == null || username.trim().isEmpty()) return;

            long nights = ChronoUnit.DAYS.between(in, out);
            double total = nights * room.getPrice();
            String[] methods = {"UPI", "Card", "Cash at Hotel"};
        String method = (String) JOptionPane.showInputDialog(
                f, "Select Payment Method:", "Hotel Payment", JOptionPane.QUESTION_MESSAGE,
                null, methods, methods[0]);
         if (method == null) {
                JOptionPane.showMessageDialog(f, "Booking cancelled. No booking was saved.");
                return;
            }
            String status = method.equals("Cash at Hotel") ? "Pending" : "Paid (Simulation)";
          Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(f, "Cannot connect to MySQL. Check your database settings.");
                return;
            }

     String bookingSql = "INSERT INTO bookings (username, hotel_name, room_type, price, booking_date, check_in, check_out) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String paymentSql = "INSERT INTO payments (username, hotel_name, amount, payment_method, payment_status, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                con.setAutoCommit(false);
                try (PreparedStatement ps = con.prepareStatement(bookingSql)) {
                    ps.setString(1, username.trim());
                 ps.setString(2, hotel);
                    ps.setString(3, room.getRoomType());
                    ps.setDouble(4, room.getPrice());
                ps.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                    ps.setDate(6, java.sql.Date.valueOf(in));
                    ps.setDate(7, java.sql.Date.valueOf(out));
                 ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
                    ps.setString(1, username.trim());
                    ps.setString(2, hotel);
                    ps.setDouble(3, total);
                    ps.setString(4, method);
                 ps.setString(5, status);
                    ps.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException ex) {
                try { con.rollback(); } catch (SQLException ignored) { }
               JOptionPane.showMessageDialog(f, "Booking failed: " + ex.getMessage());
                return;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException ignored) { }
            }

            JOptionPane.showMessageDialog(f,
                "PAYMENT CONFIRMATION\n\nHotel: " + hotel
             + "\nRoom Type: " + room.getRoomType()
                + "\nRoom Number: " + room.getRoomNumber()
                + "\nAmount: Rs. " + total
                + "\nMethod: " + method + "\nStatus: " + status);

         JOptionPane.showMessageDialog(f,
                "       HOTEL BOOKING BILL\n\n"
            + "Username: " + username.trim() + "\n"
                + "Hotel: " + hotel + "\n"
                + "Room Type: " + room.getRoomType() + "\n"
                + "Room Number: " + room.getRoomNumber() + "\n"
                + "Check-in: " + in + "\n"
                + "Check-out: " + out + "\n"
              + "Number of Nights: " + nights + "\n"
                + "Price per Night: Rs. " + room.getPrice() + "\n"
                + "-----------------------------\nTOTAL AMOUNT: Rs. " + total,
                "Booking Bill", JOptionPane.INFORMATION_MESSAGE);
        });

    backtodash.addActionListener(e -> {
           f.dispose();
            new Login();
        });
      f.setSize(620, 650);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      f.setLocationRelativeTo(null);
    f.setVisible(true);
    }

 public static void main(String[] args) {
        new Hotel();
    }
}
