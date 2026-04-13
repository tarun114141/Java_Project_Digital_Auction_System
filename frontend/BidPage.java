package frontend;

import com.auction.dao.ItemDao;
import com.auction.dao.BidDao;
import com.auction.entities.Item;
import com.auction.entities.Bid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class BidPage extends JPanel {
    private MainFrame frame;
    private int currentItemId = -1;

    private JLabel itemName;
    private JLabel itemDesc;
    private JLabel currentV;
    private JTextField bidAmountField;
    private JPanel bidHistoryPanel;

    private final ItemDao itemDao = new ItemDao();
    private final BidDao  bidDao  = new BidDao();

    public BidPage(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JButton backBtn = Theme.createButton("< Back", new Color(75, 85, 99));
        backBtn.addActionListener(e -> frame.navigateTo("HOME"));
        header.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Place a Bid");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.CARD_BG);
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        itemName = new JLabel("Item Name");
        itemName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        itemName.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemDesc = new JLabel("Description...");
        itemDesc.setFont(Theme.REGULAR_FONT);
        itemDesc.setForeground(Theme.TEXT_LIGHT);
        itemDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentV = new JLabel("Current Highest Bid: $0.0");
        currentV.setFont(Theme.HEADER_FONT);
        currentV.setForeground(Theme.SECONDARY_COLOR);
        currentV.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bid input row
        JPanel bidPanel = new JPanel();
        bidPanel.setBackground(Theme.CARD_BG);
        bidPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bidPanel.add(new JLabel("Your Bid ($):"));
        bidAmountField = new JTextField(10);
        bidAmountField.setFont(Theme.REGULAR_FONT);
        bidPanel.add(bidAmountField);

        JButton placeBidBtn = Theme.createButton("Submit Bid", Theme.PRIMARY_COLOR);
        placeBidBtn.addActionListener(e -> submitBid());
        bidPanel.add(placeBidBtn);

        // Bid history panel
        bidHistoryPanel = new JPanel();
        bidHistoryPanel.setLayout(new BoxLayout(bidHistoryPanel, BoxLayout.Y_AXIS));
        bidHistoryPanel.setBackground(Theme.CARD_BG);

        JLabel historyTitle = new JLabel("Recent Bids");
        historyTitle.setFont(Theme.HEADER_FONT);
        historyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(itemName);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(itemDesc);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(currentV);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(bidPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(historyTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(bidHistoryPanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Theme.BACKGROUND);
        centerWrapper.add(contentPanel);

        JScrollPane scroll = new JScrollPane(centerWrapper);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void loadItem(int itemId) {
        this.currentItemId = itemId;

        // Load item from DB
        List<Item> all = itemDao.getAllAvailableItems();
        Item item = null;
        for (Item i : all) {
            if (i.getId() == itemId) { item = i; break; }
        }

        if (item != null) {
            itemName.setText(item.getName());
            itemDesc.setText("<html><center>" + item.getDescription() + "</center></html>");

            // Get highest bid from DB
            Bid highest = bidDao.getHighestBid(itemId);
            double minPrice = (highest != null) ? highest.getAmount() : item.getStartingPrice();
            currentV.setText("Current Price: $" + String.format("%.2f", minPrice));
            bidAmountField.setText(String.format("%.2f", minPrice + 10));

            // Load bid history
            refreshBidHistory(itemId);
        }
    }

    private void refreshBidHistory(int itemId) {
        bidHistoryPanel.removeAll();
        List<Bid> bids = bidDao.getBidsForItem(itemId);
        if (bids.isEmpty()) {
            JLabel none = new JLabel("No bids yet. Be the first!");
            none.setForeground(Theme.TEXT_LIGHT);
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            bidHistoryPanel.add(none);
        } else {
            for (Bid b : bids) {
                JLabel row = new JLabel("Buyer ID " + b.getBuyerId() + "  →  $" + String.format("%.2f", b.getAmount()));
                row.setFont(Theme.REGULAR_FONT);
                row.setAlignmentX(Component.CENTER_ALIGNMENT);
                bidHistoryPanel.add(row);
                bidHistoryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        bidHistoryPanel.revalidate();
        bidHistoryPanel.repaint();
    }

    private void submitBid() {
        if (currentItemId == -1) return;

        String text = bidAmountField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a bid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(text);
            int userId = (frame.getCurrentUser() != null) ? frame.getCurrentUser().getId() : -1;

            if (userId == -1) {
                JOptionPane.showMessageDialog(this, "Please log in to place a bid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate against current highest bid
            Bid highest = bidDao.getHighestBid(currentItemId);
            double minPrice = (highest != null) ? highest.getAmount() : 0;

            if (amount <= minPrice) {
                JOptionPane.showMessageDialog(this,
                    "Your bid must be higher than the current price: $" + String.format("%.2f", minPrice),
                    "Low Bid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Save bid to Oracle DB
            Bid bid = new Bid(0, userId, currentItemId, amount);
            boolean success = bidDao.placeBid(bid);

            if (success) {
                JOptionPane.showMessageDialog(this, "Bid placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadItem(currentItemId); // refresh UI
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place bid. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
