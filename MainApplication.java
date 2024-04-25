// MainApplication.java
public class MainApplication {
    public static void main(String[] args) {
        String username = ""; // Retrieve the username from login or session
        String role = ""; // Retrieve the user's role from the database

        if (role.equals("customer")) {
            Dashboard customerDashboard = new Dashboard(username);
            customerDashboard.setVisible(true);
        } else if (role.equals("admin")) {
            AdminDashboard adminDashboard = new AdminDashboard(username);
            adminDashboard.setVisible(true);
        } else if (role.equals("agent")) {
            AgentDashboard agentDashboard = new AgentDashboard(username);
            agentDashboard.setVisible(true);
        } else {
            // Handle invalid or unrecognized roles
            System.out.println("Invalid user role!");
        }
    }
}
