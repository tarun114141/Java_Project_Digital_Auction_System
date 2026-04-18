package frontend;

import core.DatabaseConnection;
import entities.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;

    public MainFrame() {
        setTitle("Digital Auction System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize pages - no AuctionSystem needed, DAOs handle DB directly
        mainPanel.add(new LoginPage(this),  "LOGIN");
        mainPanel.add(new SignupPage(this), "SIGNUP");
        mainPanel.add(new HomePage(this),   "HOME");
        mainPanel.add(new BidPage(this),    "BID");
        mainPanel.add(new WinnersPage(this),"WINNERS");
        mainPanel.add(new PaymentsPage(this),"PAYMENTS");
        mainPanel.add(new AdminPage(this),  "ADMIN");

        add(mainPanel);

        // Close DB connection cleanly when window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                DatabaseConnection.closeConnection();
            }
        });
    }

    public void navigateTo(String pageName) {
        if (pageName.equals("HOME")) {
            for (Component c : mainPanel.getComponents()) {
                if (c instanceof HomePage) {
                    ((HomePage) c).refreshData();
                }
            }
        } else if (pageName.equals("WINNERS")) {
            for (Component c : mainPanel.getComponents()) {
                if (c instanceof WinnersPage) {
                    ((WinnersPage) c).refreshData();
                }
            }
        } else if (pageName.equals("PAYMENTS")) {
            for (Component c : mainPanel.getComponents()) {
                if (c instanceof PaymentsPage) {
                    ((PaymentsPage) c).refreshData();
                }
            }
        } else if (pageName.equals("ADMIN")) {
            for (Component c : mainPanel.getComponents()) {
                if (c instanceof AdminPage) {
                    ((AdminPage) c).refreshData();
                }
            }
        }
        cardLayout.show(mainPanel, pageName);
    }

    public void navigateToBid(int itemId) {
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof BidPage) {
                ((BidPage) c).loadItem(itemId);
            }
        }
        cardLayout.show(mainPanel, "BID");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
