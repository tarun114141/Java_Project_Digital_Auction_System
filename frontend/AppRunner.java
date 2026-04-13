package frontend;

import com.auction.core.DatabaseConnection;

import javax.swing.*;

public class AppRunner {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test DB connection on startup
        if (DatabaseConnection.getConnection() == null) {
            JOptionPane.showMessageDialog(null,
                "Could not connect to the Oracle database.\n" +
                "Please make sure Oracle XE is running and credentials are correct.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.navigateTo("LOGIN");
        });
    }
}
