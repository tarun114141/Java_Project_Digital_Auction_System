package frontend;

import dao.AuctionDao;
import dao.ItemDao;
import dao.CategoryDao;
import dao.BidDao;
import entities.AuctionEvent;
import entities.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HomePage extends JPanel {
    private MainFrame frame;
    private JPanel gridPanel;
    private JButton addItemBtn;
    private JLabel title;
    
    private final ItemDao itemDao = new ItemDao();
    private final AuctionDao auctionDao = new AuctionDao();
    private final CategoryDao categoryDao = new CategoryDao();
    private final BidDao bidDao = new BidDao();

    private JPanel filterPanel;
    private JTextField searchField;
    private JComboBox<entities.Category> categoryCombo;
    private JPanel warningPanel;
    private JLabel warningLabel;

    public HomePage(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Wrap everything top in a container for the warning panel
        JPanel topContainer = new JPanel(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Theme.PRIMARY_COLOR);
        title = new JLabel("Available Auctions");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        JLabel userLabel = new JLabel("Welcome!");
        userLabel.setFont(Theme.REGULAR_FONT);
        userLabel.setForeground(Color.WHITE);
        titlePanel.add(userLabel);
        
        header.add(titlePanel, BorderLayout.WEST);

        // Filter center panel (For search / categories)
        filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filterPanel.setBackground(Theme.PRIMARY_COLOR);
        searchField = new JTextField(15);
        searchField.setFont(Theme.REGULAR_FONT);
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(Theme.REGULAR_FONT);
        categoryCombo.addItem(new entities.Category(0, "All Categories", ""));
        for (entities.Category c : categoryDao.getAllCategories()) {
            categoryCombo.addItem(c);
        }
        JButton searchBtn = Theme.createButton("Search", new Color(14, 165, 233));
        searchBtn.addActionListener(e -> refreshData());
        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setForeground(Color.WHITE);
        searchLbl.setFont(Theme.REGULAR_FONT);
        filterPanel.add(searchLbl);
        filterPanel.add(searchField);
        filterPanel.add(categoryCombo);
        filterPanel.add(searchBtn);
        header.add(filterPanel, BorderLayout.CENTER);

        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightSection.setBackground(Theme.PRIMARY_COLOR);

        addItemBtn = Theme.createButton("Add Item", new Color(34, 197, 94));
        addItemBtn.setVisible(false); // Initially hidden
        addItemBtn.addActionListener(e -> showAddItemDialog());
        rightSection.add(addItemBtn);

        JButton winnersBtn = Theme.createButton("Winners", Theme.SECONDARY_COLOR);
        winnersBtn.addActionListener(e -> frame.navigateTo("WINNERS"));
        rightSection.add(winnersBtn);

        JButton paymentsBtn = Theme.createButton("Payments", new Color(14, 165, 233));
        paymentsBtn.addActionListener(e -> frame.navigateTo("PAYMENTS"));
        rightSection.add(paymentsBtn);

        JButton logoutBtn = Theme.createButton("Logout", new Color(220, 38, 38));
        logoutBtn.addActionListener(e -> {
            frame.setCurrentUser(null);
            frame.navigateTo("LOGIN");
        });
        rightSection.add(logoutBtn);

        header.add(rightSection, BorderLayout.EAST);
        topContainer.add(header, BorderLayout.NORTH);

        // Warning Panel setup
        warningPanel = new JPanel();
        warningPanel.setBackground(new Color(220, 38, 38)); // Red color
        warningPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        warningLabel = new JLabel("");
        warningLabel.setFont(Theme.HEADER_FONT);
        warningLabel.setForeground(Color.WHITE);
        warningPanel.add(warningLabel);
        warningPanel.setVisible(false);

        topContainer.add(warningPanel, BorderLayout.SOUTH);
        
        add(topContainer, BorderLayout.NORTH);

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
        boolean isSeller = false;
        boolean isBuyer = false;
        if (frame.getCurrentUser() != null) {
            isSeller = "SELLER".equalsIgnoreCase(frame.getCurrentUser().getRole());
            isBuyer = "BUYER".equalsIgnoreCase(frame.getCurrentUser().getRole());
            addItemBtn.setVisible(isSeller);
            filterPanel.setVisible(isBuyer);
        }

        if (isBuyer) {
            List<String> outbidItems = bidDao.getOutbidItemNames(frame.getCurrentUser().getId());
            if (!outbidItems.isEmpty()) {
                String itemsStr = String.join(", ", outbidItems);
                warningLabel.setText("⚠️ ALERT: You have been outbid on: " + itemsStr + ". Place a new bid!");
                warningPanel.setVisible(true);
            } else {
                warningPanel.setVisible(false);
            }
        } else {
            warningPanel.setVisible(false);
        }

        gridPanel.removeAll();

        List<Item> items;
        if (isSeller) {
            title.setText("My Listed Items");
            items = itemDao.getItemsBySeller(frame.getCurrentUser().getId());
        } else {
            title.setText("Available Auctions");
            String kw = searchField.getText();
            entities.Category cat = (entities.Category) categoryCombo.getSelectedItem();
            int catId = (cat != null) ? cat.getId() : 0;
            items = itemDao.getFilteredAvailableItems(kw, catId);
        }

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

        JLabel timeline = new JLabel("Fetching timeline...");
        timeline.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        AuctionEvent auction = auctionDao.findById(item.getAuctionId());
        if (auction != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
            if ("COMPLETED".equals(auction.getStatus()) || (!auction.isActive() && auction.getEndTime().before(new java.util.Date()))) {
                timeline.setText("Ended: " + sdf.format(auction.getEndTime()));
                timeline.setForeground(new Color(220, 38, 38));
            } else if ("UPCOMING".equals(auction.getStatus()) || auction.getStartTime().after(new java.util.Date())) {
                timeline.setText("Starts: " + sdf.format(auction.getStartTime()));
                timeline.setForeground(new Color(234, 179, 8));
            } else {
                timeline.setText("Closes: " + sdf.format(auction.getEndTime()));
                timeline.setForeground(new Color(34, 197, 94));
            }
        } else {
            timeline.setText("No timeline");
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setBackground(Theme.CARD_BG);

        JButton bidBtn = Theme.createButton("View / Bid", Theme.PRIMARY_COLOR);
        bidBtn.addActionListener(e -> frame.navigateToBid(item.getId()));
        buttonPanel.add(bidBtn);

        // Add Delete Button if current user is the owner
        if (frame.getCurrentUser() != null && frame.getCurrentUser().getId() == item.getSellerId()) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton deleteBtn = Theme.createButton("Delete", new Color(220, 38, 38)); // Red color
            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (itemDao.deleteItem(item.getId())) {
                        JOptionPane.showMessageDialog(frame, "Item deleted.");
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete item. It might have bids.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(deleteBtn);
        }

        card.add(name);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(price);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(timeline);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(buttonPanel);

        return card;
    }

    private void showAddItemDialog() {
        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JTextField priceField = new JTextField(10);
        JTextField imgField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Starting Price ($):"));
        panel.add(priceField);
        panel.add(new JLabel("Image Filename:"));
        panel.add(imgField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Auction Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                String img = imgField.getText().isEmpty() ? "default.png" : imgField.getText();
                
                Item newItem = new Item(0, name, desc, price, img, frame.getCurrentUser().getId(), 1, 1);
                newItem.setStatus("AVAILABLE");

                if (itemDao.addItem(newItem)) {
                    JOptionPane.showMessageDialog(frame, "Item added successfully!");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add item to database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
