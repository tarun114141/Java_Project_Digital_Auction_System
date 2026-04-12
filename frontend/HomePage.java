package frontend;

import com.auction.services.AuctionSystem;
import com.auction.entities.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HomePage extends JPanel {
    private MainFrame frame;
    private AuctionSystem system;
    private JPanel gridPanel;

    public HomePage(MainFrame frame, AuctionSystem system) {
        this.frame = frame;
        this.system = system;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Available Auctions");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JButton logoutBtn = Theme.createButton("Logout", new Color(220, 38, 38));
        logoutBtn.addActionListener(e -> frame.navigateTo("LOGIN"));
        header.add(logoutBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Items Grid
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(Theme.BACKGROUND);
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        gridPanel.removeAll();
        List<Item> items = system.browseItems();
        
        for (Item item : items) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Theme.CARD_BG);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            JLabel name = new JLabel(item.getName());
            name.setFont(Theme.HEADER_FONT);
            
            JLabel price = new JLabel("Start Price: $" + item.getStartingPrice());
            price.setFont(Theme.REGULAR_FONT);
            price.setForeground(Theme.SECONDARY_COLOR);

            JButton bidBtn = Theme.createButton("View / Bid", Theme.PRIMARY_COLOR);
            bidBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            bidBtn.addActionListener(e -> frame.navigateToBid(item.getId()));

            card.add(name);
            card.add(Box.createRigidArea(new Dimension(0, 10)));
            card.add(price);
            card.add(Box.createRigidArea(new Dimension(0, 15)));
            card.add(bidBtn);

            gridPanel.add(card);
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
