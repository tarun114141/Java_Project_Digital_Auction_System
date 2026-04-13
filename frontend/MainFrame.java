package frontend;

import com.auction.core.DatabaseConnection;
import com.auction.entities.User;

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
