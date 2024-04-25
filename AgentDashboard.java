import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgentDashboard extends JFrame implements ActionListener {
    JButton addPackageButton;
    JButton deletePackageButton;
    JButton addHotelButton;
    JButton deleteHotelButton;
    Connection con;
    String agentUsername; // Store the agent's username
    JTextArea packagesTextArea; // Declare as instance variable
    JTextArea hotelsTextArea; // Declare as instance variable

    AgentDashboard(String username) {
        this.agentUsername = username; // Store the agent's username
        setBounds(500, 200, 1000, 800);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Agent Dashboard");
        titleLabel.setBounds(120, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel);

        addPackageButton = new JButton("Add Package");
        addPackageButton.setBounds(100, 80, 200, 30);
        addPackageButton.addActionListener(this);
        add(addPackageButton);

        deletePackageButton = new JButton("Delete Package");
        deletePackageButton.setBounds(100, 130, 200, 30);
        deletePackageButton.addActionListener(this);
        add(deletePackageButton);

        addHotelButton = new JButton("Add Hotel");
        addHotelButton.setBounds(100, 180, 200, 30);
        addHotelButton.addActionListener(this);
        add(addHotelButton);

        deleteHotelButton = new JButton("Delete Hotel");
        deleteHotelButton.setBounds(100, 230, 200, 30);
        deleteHotelButton.addActionListener(this);
        add(deleteHotelButton);

        // List Packages
        JLabel packagesLabel = new JLabel("Packages:");
        packagesLabel.setBounds(350, 20, 100, 20);
        packagesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(packagesLabel);

        packagesTextArea = new JTextArea(); // Initialize instance variable
        packagesTextArea.setEditable(false);
        packagesTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        packagesTextArea.setForeground(Color.BLUE);
        JScrollPane packagesScrollPane = new JScrollPane(packagesTextArea);
        packagesScrollPane.setBounds(350, 50, 250, 150);
        add(packagesScrollPane);

        // List Hotels
        JLabel hotelsLabel = new JLabel("Hotels:");
        hotelsLabel.setBounds(650,20, 100, 20);
        hotelsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(hotelsLabel);

        hotelsTextArea = new JTextArea(); // Initialize instance variable
        hotelsTextArea.setEditable(false);
        hotelsTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        hotelsTextArea.setForeground(Color.GREEN);
        JScrollPane hotelsScrollPane = new JScrollPane(hotelsTextArea);
        hotelsScrollPane.setBounds(650, 50, 200, 150);
        add(hotelsScrollPane);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(300, 200, 400, 400);
        try {
            // Load image from file
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("agent.png"));
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
            displayPackages(packagesTextArea);
            displayHotels(hotelsTextArea);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addPackageButton) {
            new AddPackageWindow().setVisible(true);
        } else if (ae.getSource() == deletePackageButton) {
            // Handle deleting a package
            String packageName = JOptionPane.showInputDialog(this, "Enter package name to delete:");
            if (packageName != null && !packageName.isEmpty()) {
                deletePackage(packageName);
            } else {
                JOptionPane.showMessageDialog(this, "Package name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == addHotelButton) {
            new AddHotelWindow().setVisible(true);
        } else if (ae.getSource() == deleteHotelButton) {
            // Handle deleting a hotel
            String hotelName = JOptionPane.showInputDialog(this, "Enter hotel name to delete:");
            if (hotelName != null && !hotelName.isEmpty()) {
                deleteHotel(hotelName);
            } else {
                JOptionPane.showMessageDialog(this, "Hotel name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayPackages(JTextArea packagesTextArea) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT name FROM packages WHERE agent = ?");
            stmt.setString(1, agentUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                packagesTextArea.append(String.format("%-20s%n", rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayHotels(JTextArea hotelsTextArea) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT name FROM hotel WHERE agent = ?");
            stmt.setString(1, agentUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hotelsTextArea.append(String.format("%-20s%n", rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addPackage(String name, String cost) {
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO packages (name, cost, agent) VALUES (?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, cost);
            pstmt.setString(3, agentUsername);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Package added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Update the packages text area
                displayPackages(packagesTextArea);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add package.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deletePackage(String name) {
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM packages WHERE name = ? AND agent = ?");
            pstmt.setString(1, name);
            pstmt.setString(2, agentUsername);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Package deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear and update the packages text area
                packagesTextArea.setText(""); // Clear the text area
                displayPackages(packagesTextArea); // Reload updated data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete package.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void addHotel(String name, String costPerDay, String acCharges, String foodCharges) {
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO hotel (name, cost_per_day, ac_charges, food_charges, agent) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, costPerDay);
            pstmt.setString(3, acCharges);
            pstmt.setString(4, foodCharges);
            pstmt.setString(5, agentUsername);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hotel added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Update the hotels text area
                displayHotels(hotelsTextArea);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add hotel.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteHotel(String name) {
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM hotel WHERE name = ? AND agent = ?");
            pstmt.setString(1, name);
            pstmt.setString(2, agentUsername);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hotel deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear and update the hotels text area
                hotelsTextArea.setText(""); // Clear the text area
                displayHotels(hotelsTextArea); // Reload updated data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete hotel.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    class AddPackageWindow extends JFrame implements ActionListener {
        // Components for adding a package
        JTextField packageNameField;
        JTextField packageCostField;

        JButton addButton;

        AddPackageWindow() {
            setBounds(300, 150, 400, 300);
            setLayout(null);
            getContentPane().setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Add Package");
            titleLabel.setBounds(150, 20, 200, 30);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(titleLabel);

            JLabel nameLabel = new JLabel("Package Name:");
            nameLabel.setBounds(50, 80, 150, 20);
            add(nameLabel);

            packageNameField = new JTextField();
            packageNameField.setBounds(200, 80, 150, 20);
            add(packageNameField);

            JLabel costLabel = new JLabel("Package Cost:");
            costLabel.setBounds(50, 120, 150, 20);
            add(costLabel);

            packageCostField = new JTextField();
            packageCostField.setBounds(200, 120, 150, 20);
            add(packageCostField);

            addButton = new JButton("Add");
            addButton.setBounds(150, 180, 100, 30);
            addButton.addActionListener(this);
            add(addButton);
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addButton) {
                String packageName = packageNameField.getText();
                String packageCost = packageCostField.getText();
                addPackage(packageName, packageCost);
            }
        }
    }

    class AddHotelWindow extends JFrame implements ActionListener {
        // Components for adding a hotel
        JTextField hotelNameField;
        JTextField costPerDayField;
        JTextField acChargesField;
        JTextField foodChargesField;

        JButton addButton;

        AddHotelWindow() {
            setBounds(300, 150, 400, 300);
            setLayout(null);
            getContentPane().setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Add Hotel");
            titleLabel.setBounds(150, 20, 200, 30);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(titleLabel);

            JLabel nameLabel = new JLabel("Hotel Name:");
            nameLabel.setBounds(50, 50, 150, 20);
            add(nameLabel);

            hotelNameField = new JTextField();
            hotelNameField.setBounds(200, 50, 150, 20);
            add(hotelNameField);

            JLabel costPerDayLabel = new JLabel("Cost Per Day:");
            costPerDayLabel.setBounds(50, 90, 150, 20);
            add(costPerDayLabel);

            costPerDayField = new JTextField();
            costPerDayField.setBounds(200, 90, 150, 20);
            add(costPerDayField);

            JLabel acChargesLabel = new JLabel("AC Charges:");
            acChargesLabel.setBounds(50, 130, 150, 20);
            add(acChargesLabel);

            acChargesField = new JTextField();
            acChargesField.setBounds(200, 130, 150, 20);
            add(acChargesField);

            JLabel foodChargesLabel = new JLabel("Food Charges:");
            foodChargesLabel.setBounds(50, 170, 150, 20);
            add(foodChargesLabel);

            foodChargesField = new JTextField();
            foodChargesField.setBounds(200, 170, 150, 20);
            add(foodChargesField);

            addButton = new JButton("Add");
            addButton.setBounds(150, 220, 100, 30);
            addButton.addActionListener(this);
            add(addButton);
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addButton) {
                String hotelName = hotelNameField.getText();
                String costPerDay = costPerDayField.getText();
                String acCharges = acChargesField.getText();
                String foodCharges = foodChargesField.getText();
                addHotel(hotelName, costPerDay, acCharges, foodCharges);
            }
        }
    }

    public static void main(String[] args) {
        new AgentDashboard("").setVisible(true);
    }
}
