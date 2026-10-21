import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Register {
    Register() {
        JFrame f = new JFrame("Hotel Booking");

        JLabel title = new JLabel("Welcome to Hotel Booking System");
        title.setHorizontalAlignment(JLabel.CENTER);
        JLabel us = new JLabel("Username:");
        JLabel ph = new JLabel("Phone Number:");
        JLabel pas = new JLabel("Password:");
        JLabel cpas = new JLabel("Confirm Password:");

        JTextField username = new JTextField(15);
        JTextField phonenumber = new JTextField(15);
        JPasswordField password = new JPasswordField(15);
        JPasswordField cpassword = new JPasswordField(15);

        JButton btn = new JButton("Register");
        JButton back=new JButton("Back to Login");

        JPanel p = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

        title.setFont(new Font("Arial", Font.BOLD, 20));

        form.add(us);
        form.add(username);
        form.add(ph);
    form.add(phonenumber);
       form.add(pas);
        form.add(password);
    form.add(cpas);
    form.add(cpassword);
        form.add(back);
     form.add(btn);

        p.add(form);

        f.setLayout(new BorderLayout(10, 20));
        f.add(title, BorderLayout.NORTH);
        f.add(p, BorderLayout.CENTER);

        btn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
                String pass=new String(password.getPassword());
             String cpass=new String(cpassword.getPassword());

                if(pass.equals(cpass)){
                try{
                    Connection con=DatabaseConnection.getInstance().getConnection();

                    String sql="INSERT INTO users (username, phone, password) VALUES (?,?,?)";
                    PreparedStatement ps=con.prepareStatement(sql);

                  ps.setString(1, username.getText());
                    ps.setString(2, phonenumber.getText());
                    ps.setString(3, pass);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(f, "Registration Sucessfully!");
              }catch(SQLException ex){
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
                else{
                    JOptionPane.showMessageDialog(f, "Password do not match!");
                }

            }
  });
        
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                f.dispose();
                new Login();
            }
        });

      f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    public static void main(String[] args) {
        new Register();
    }
}
