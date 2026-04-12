package frontend;

import com.auction.services.AuctionSystem;
import com.auction.entities.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private AuctionSystem auctionSystem;
    private User currentUser;

    public MainFrame(AuctionSystem system) {
        this.auctionSystem = system;
        setTitle("Digital Auction System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize pages
        mainPanel.add(new LoginPage(this, system), "LOGIN");
        mainPanel.add(new HomePage(this, system), "HOME");
        mainPanel.add(new BidPage(this, system), "BID");

        add(mainPanel);
    }

    public void navigateTo(String pageName) {
        if (pageName.equals("HOME")) {
            // refresh home page items
            Component[] comps = mainPanel.getComponents();
            for (Component c : comps) {
                if (c instanceof HomePage) {
                    ((HomePage) c).refreshData();
                }
            }
        }
        cardLayout.show(mainPanel, pageName);
    }
    
    public void navigateToBid(int itemId) {
        Component[] comps = mainPanel.getComponents();
        for (Component c : comps) {
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
