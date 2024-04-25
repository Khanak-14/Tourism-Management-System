import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.event.*;

public class Paytm extends JFrame implements ActionListener {

    Paytm() {
        setTitle("Paytm Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        try {
            editorPane.setPage("https://paytm.com/credit-card-bill-payment");
        } catch (Exception e) {
            editorPane.setContentType("text/html");
            editorPane.setText("<html>Could not load, Error 404 <html>");
        }

        JScrollPane scrollPane = new JScrollPane(editorPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);

        setSize(700, 580);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Payment().setVisible(true);
    }
}