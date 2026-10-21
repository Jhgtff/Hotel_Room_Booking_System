import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Dashboard {
    Dashboard(String username) {
        JFrame f = new JFrame("Hotel Booking Dashboard");

        JLabel title = new JLabel("Welcome to Hotel Booking System");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton view = new JButton("View Rooms");
        JButton book = new JButton("Book a Room");
        JButton bookings = new JButton("My Bookings");
        JButton logout = new JButton("Logout");

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel buttons = new JPanel(new GridLayout(4, 1, 10, 10));

        buttons.setPreferredSize(new Dimension(180, 160));

        buttons.add(view);
        buttons.add(book);
        buttons.add(bookings);
        buttons.add(logout);

        p.add(buttons);

        f.setLayout(new BorderLayout(10, 20));
        f.add(title, BorderLayout.NORTH);
        f.add(p, BorderLayout.CENTER);

        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new Login();
            }
        });

        view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{
                    Connection con=DatabaseConnection.getInstance().getConnection();

                    String sql="SELECT * FROM rooms";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ResultSet rs=ps.executeQuery();

                    String [] columns={"Room Number","Room Type","Price"};
                    DefaultTableModel model=new DefaultTableModel(columns,0);

                    while(rs.next()){
                        Object[] row ={
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        "₹" +rs.getDouble("price")
                    };
                    model.addRow(row);
                    }
                    JTable table=new JTable(model);
                    JScrollPane scroll=new JScrollPane(table);

                    JOptionPane.showMessageDialog(f, scroll, "Available Rooms", JOptionPane.INFORMATION_MESSAGE);

                    rs.close();
                    ps.close();
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
        });

        book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                f.dispose();
                new Hotel();
            }
        });

        
bookings.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        try {
            Connection con = DatabaseConnection.getInstance()
                .getConnection();

            String sql = "SELECT * FROM bookings where username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            String[] columns = {
                "Booking ID", "Username", "Hotel",
                "Room Type", "Price", "Booking Date"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("booking_id"),
                    rs.getString("username"),
                    rs.getString("hotel_name"),
                    rs.getString("room_type"),
                    "₹" + rs.getDouble("price"),
                    rs.getDate("booking_date")
                };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scroll = new JScrollPane(table);

            JOptionPane.showMessageDialog(
                f, scroll, "My Bookings",
                JOptionPane.INFORMATION_MESSAGE
            );

            rs.close();
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(f, ex.getMessage());
        }
    }
});

        f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}