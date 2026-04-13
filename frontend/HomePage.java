package frontend;

import com.auction.dao.ItemDao;
import com.auction.entities.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HomePage extends JPanel {
    private MainFrame frame;
    private JPanel gridPanel;
    private final ItemDao itemDao = new ItemDao();

    public HomePage(MainFrame frame) {
        this.frame = frame;
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

        // Show logged-in user greeting
        JLabel userLabel = new JLabel("");
        userLabel.setFont(Theme.REGULAR_FONT);
        userLabel.setForeground(Color.WHITE);
        header.add(userLabel, BorderLayout.CENTER);

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

        // Load items from Oracle DB via ItemDao
        List<Item> items = itemDao.getAllAvailableItems();

        if (items.isEmpty()) {
            JLabel empty = new JLabel("No items available right now.", SwingConstants.CENTER);
            empty.setFont(Theme.HEADER_FONT);
            empty.setForeground(Theme.TEXT_LIGHT);
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(empty, BorderLayout.CENTER);
        } else {
            gridPanel.setLayout(new GridLayout(0, 3, 20, 20));
            for (Item item : items) {
                gridPanel.add(createItemCard(item));
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createItemCard(Item item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel name = new JLabel(item.getName());
        name.setFont(Theme.HEADER_FONT);

        JLabel desc = new JLabel("<html><p style='width:150px'>" + item.getDescription() + "</p></html>");
        desc.setFont(Theme.REGULAR_FONT);
        desc.setForeground(Theme.TEXT_LIGHT);

        JLabel price = new JLabel("Starting Price: $" + item.getStartingPrice());
        price.setFont(Theme.REGULAR_FONT);
        price.setForeground(Theme.SECONDARY_COLOR);

        JButton bidBtn = Theme.createButton("View / Bid", Theme.PRIMARY_COLOR);
        bidBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        bidBtn.addActionListener(e -> frame.navigateToBid(item.getId()));

        card.add(name);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(price);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(bidBtn);

        return card;
    }
}
