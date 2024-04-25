import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class AdminDashboard extends JFrame implements ActionListener {
    JButton viewCustomersButton;
    JButton viewAgentsButton;
    JButton deleteCustomerButton; // New button for deleting customer
    JButton deleteAgentButton;

    Connection con;

    AdminDashboard(String username) {
        setBounds(500, 200, 1000, 600);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setBounds(20, 20, 300, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel);

        viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.setBounds(300, 100, 200, 40);
        viewCustomersButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewCustomersButton.setFocusPainted(false);
        viewCustomersButton.setBackground(Color.WHITE);
        viewCustomersButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        viewCustomersButton.setForeground(Color.BLUE);
        viewCustomersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewCustomersButton.setBackground(Color.BLUE);
                viewCustomersButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewCustomersButton.setBackground(Color.WHITE);
                viewCustomersButton.setForeground(Color.BLUE);
            }
        });
        viewCustomersButton.addActionListener(this);
        add(viewCustomersButton);

        viewAgentsButton = new JButton("View Agents");
        viewAgentsButton.setBounds(300, 150, 200, 40);
        viewAgentsButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewAgentsButton.setFocusPainted(false);
        viewAgentsButton.setBackground(Color.WHITE);
        viewAgentsButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        viewAgentsButton.setForeground(Color.BLUE);
        viewAgentsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewAgentsButton.setBackground(Color.BLUE);
                viewAgentsButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewAgentsButton.setBackground(Color.WHITE);
                viewAgentsButton.setForeground(Color.BLUE);
            }
        });
        viewAgentsButton.addActionListener(this);
        add(viewAgentsButton);

        deleteCustomerButton = new JButton("Delete Customer"); // Initialize delete customer button
        deleteCustomerButton.setBounds(300, 200, 200, 40);
        deleteCustomerButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteCustomerButton.setFocusPainted(false);
        deleteCustomerButton.setBackground(Color.WHITE);
        deleteCustomerButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        deleteCustomerButton.setForeground(Color.RED);
        deleteCustomerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteCustomerButton.setBackground(Color.RED);
                deleteCustomerButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteCustomerButton.setBackground(Color.WHITE);
                deleteCustomerButton.setForeground(Color.RED);
            }
        });
        deleteCustomerButton.addActionListener(this);
        add(deleteCustomerButton);

        deleteAgentButton = new JButton("Delete Agent"); // Initialize delete agent button
        deleteAgentButton.setBounds(300, 250, 200, 40);
        deleteAgentButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteAgentButton.setFocusPainted(false);
        deleteAgentButton.setBackground(Color.WHITE);
        deleteAgentButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        deleteAgentButton.setForeground(Color.RED);
        deleteAgentButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteAgentButton.setBackground(Color.RED);
                deleteAgentButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteAgentButton.setBackground(Color.WHITE);
                deleteAgentButton.setForeground(Color.RED);
            }
        });
        deleteAgentButton.addActionListener(this);
        add(deleteAgentButton);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(600, 100, 400, 400);
        try {
            // Load image from file
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("admin_photo.png"));
            Image image = imageIcon.getImage();
            Image newImage = image.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newImage);
            imageLabel.setIcon(imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(imageLabel);

        // Establish database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/travelmanagementsystem", "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == viewCustomersButton) {
            viewAllCustomers();
        } else if (ae.getSource() == viewAgentsButton) {
            viewAllAgents();
        } else if (ae.getSource() == deleteCustomerButton) {
            showDeleteCustomerDialog();
        } else if (ae.getSource() == deleteAgentButton) {
            showDeleteAgentDialog();
        }
    }

    private void viewAllCustomers() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE role = 'customer'");
            JFrame customerFrame = new JFrame("All Customers");
            customerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame on exit
            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // Improved spacing between components
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to panel

            // Add title label
            JLabel titleLabel = new JLabel("All Customers");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            customerFrame.add(titleLabel, BorderLayout.NORTH);

            while (rs.next()) {
                String customerUsername = rs.getString("username");
                String customerName = rs.getString("name");
                panel.add(createLabel("Username: " + customerUsername));
                panel.add(createLabel("Name: " + customerName));
            }
            customerFrame.add(new JScrollPane(panel)); // Add scroll pane
            customerFrame.pack();
            customerFrame.setLocationRelativeTo(null); // Center the frame on the screen
            customerFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewAllAgents() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE role = 'agent'");
            JFrame agentFrame = new JFrame("All Agents");
            agentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame on exit
            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // Improved spacing between components
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to panel

            // Add title label
            JLabel titleLabel = new JLabel("All Agents");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            agentFrame.add(titleLabel, BorderLayout.NORTH);

            while (rs.next()) {
                String agentUsername = rs.getString("username");
                String agentName = rs.getString("name");
                panel.add(createLabel("Username: " + agentUsername));
                panel.add(createLabel("Name: " + agentName));
            }
            agentFrame.add(new JScrollPane(panel)); // Add scroll pane
            agentFrame.pack();
            agentFrame.setLocationRelativeTo(null); // Center the frame on the screen
            agentFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteCustomer(String username) {
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM account WHERE username = ? AND role = 'customer'");
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found or unable to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the customer.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteAgent(String username) {
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM account WHERE username = ? AND role = 'agent'");
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Agent deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Agent not found or unable to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the agent.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showDeleteCustomerDialog() {
        JTextField usernameField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter Customer Username:"));
        panel.add(usernameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Delete Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                deleteCustomer(username);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteAgentDialog() {
        JTextField usernameField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter Agent Username:"));
        panel.add(usernameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Delete Agent", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                deleteAgent(username);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    public static void main(String[] args) {
        new AdminDashboard("admin").setVisible(true);
    }
}
