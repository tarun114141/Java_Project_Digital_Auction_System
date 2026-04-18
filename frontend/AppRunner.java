package frontend;

import core.DatabaseConnection;

import javax.swing.*;
import java.awt.BorderLayout;

public class AppRunner {
    public static void main(String[] args) {
        // Show Splash Screen immediately
        JWindow splash = new JWindow();
        JPanel splashPanel = new JPanel(new BorderLayout());
        splashPanel.setBackground(new java.awt.Color(37, 99, 235));
        splashPanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.WHITE, 3));
        
        JLabel title = new JLabel("Digital Auction System", SwingConstants.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        title.setForeground(java.awt.Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
        splashPanel.add(title, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Connecting to Database...");
        progressBar.setBackground(new java.awt.Color(37, 99, 235));
        progressBar.setForeground(java.awt.Color.WHITE);
        splashPanel.add(progressBar, BorderLayout.SOUTH);

        splash.getContentPane().add(splashPanel);
        splash.setSize(450, 150);
        splash.setLocationRelativeTo(null); // center
        splash.setVisible(true);

        // Run heavy initialization in a background thread
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Set FlatLaf modern look and feel
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                } catch (Exception e) {
                    try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ex) {}
                }

                // Test DB connection
                if (DatabaseConnection.getConnection() == null) {
                    SwingUtilities.invokeLater(() -> {
                        splash.dispose();
                        JOptionPane.showMessageDialog(null,
                            "Could not connect to the Oracle database.\nPlease make sure Oracle XE is running.",
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    });
                }
                Thread.sleep(1000); // artificially show the cool splash screen for 1s
                return null;
            }

            @Override
            protected void done() {
                splash.dispose();
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                frame.navigateTo("LOGIN");
            }
        }.execute();
    }
}
