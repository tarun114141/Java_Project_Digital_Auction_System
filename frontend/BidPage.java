package frontend;

import com.auction.services.AuctionSystem;
import com.auction.entities.Item;
import com.auction.entities.Bid;
import com.auction.exceptions.AuctionException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BidPage extends JPanel {
    private MainFrame frame;
    private AuctionSystem system;
    private int currentItemId;

    private JLabel itemName;
    private JLabel itemDesc;
    private JLabel currentV;
    private JTextField bidAmountField;

    public BidPage(MainFrame frame, AuctionSystem system) {
        this.frame = frame;
        this.system = system;
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

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.CARD_BG);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

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

        JPanel bidPanel = new JPanel();
        bidPanel.setBackground(Theme.CARD_BG);
        bidPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        bidPanel.add(new JLabel("Your Bid ($):"));
        bidAmountField = new JTextField(10);
        bidPanel.add(bidAmountField);

        JButton placeBidBtn = Theme.createButton("Submit Bid", Theme.PRIMARY_COLOR);
        placeBidBtn.addActionListener(e -> submitBid());
        bidPanel.add(placeBidBtn);

        contentPanel.add(itemName);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(itemDesc);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(currentV);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(bidPanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Theme.BACKGROUND);
        centerWrapper.add(contentPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    public void loadItem(int itemId) {
        this.currentItemId = itemId;
        Item item = null;
        for (Item i : system.browseItems()) {
            if (i.getId() == itemId) {
                item = i;
                break;
            }
        }
        if (item != null) {
            itemName.setText(item.getName());
            itemDesc.setText("<html><center>" + item.getDescription() + "</center></html>");
            
            Bid highest = item.getHighestBid();
            double minPrice = (highest != null) ? highest.getAmount() : item.getStartingPrice();
            currentV.setText("Current Price: $" + minPrice);
            bidAmountField.setText(String.valueOf(minPrice + 10)); // Suggest a slightly higher bid
        }
    }

    private void submitBid() {
        try {
            double amount = Double.parseDouble(bidAmountField.getText());
            int userId = frame.getCurrentUser() != null ? frame.getCurrentUser().getId() : 1; 

            // Create a fake bid ID (in real db it would autogenerate)
            int bidId = (int) (Math.random() * 1000);
            Bid bid = new Bid(bidId, userId, currentItemId, amount);

            system.placeBid(bid);
            JOptionPane.showMessageDialog(this, "Bid Placed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadItem(currentItemId); // refresh
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (AuctionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
