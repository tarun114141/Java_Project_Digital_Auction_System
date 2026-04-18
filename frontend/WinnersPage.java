package frontend;

import dao.BidDao;
import dao.ItemDao;
import dao.UserDao;
import entities.Bid;
import entities.Item;
import entities.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WinnersPage extends JPanel {
    private MainFrame frame;
    private JTable winnersTable;
    private DefaultTableModel tableModel;
    private java.util.List<Integer> displayedWinnerIds = new java.util.ArrayList<>();

    private final ItemDao itemDao = new ItemDao();
    private final BidDao bidDao = new BidDao();
    private final UserDao userDao = new UserDao();

    public WinnersPage(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Winners & Leading Bids");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        // Buttons
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightSection.setBackground(Theme.PRIMARY_COLOR);

        JButton detailsBtn = Theme.createButton("View Winner Details", new Color(14, 165, 233)); // Light blue
        detailsBtn.addActionListener(e -> showWinnerDetails());
        rightSection.add(detailsBtn);

        JButton homeBtn = Theme.createButton("Back to Home", Theme.SECONDARY_COLOR);
        homeBtn.addActionListener(e -> frame.navigateTo("HOME"));
        rightSection.add(homeBtn);

        header.add(rightSection, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Item Name", "Status", "Starting Price", "Highest Bid", "Winner / Leader"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        winnersTable = new JTable(tableModel);
        winnersTable.setFont(Theme.REGULAR_FONT);
        winnersTable.setRowHeight(30);
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setBackground(Theme.PRIMARY_COLOR);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(Theme.HEADER_FONT);
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        winnersTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(winnersTable);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Theme.BACKGROUND);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0); // clear existing data
        displayedWinnerIds.clear();

        List<Item> items = itemDao.getAllItems();
        
        for (Item item : items) {
            Bid highestBid = bidDao.getHighestBid(item.getId());
            String highestBidStr = "No bids";
            String leaderStr = "N/A";
            int winnerId = -1;

            if (highestBid != null) {
                winnerId = highestBid.getBuyerId();
                highestBidStr = "$" + highestBid.getAmount();
                User leader = userDao.findById(winnerId);
                if (leader != null) {
                    leaderStr = leader.getName();
                } else {
                    leaderStr = "Unknown (ID: " + winnerId + ")";
                }
            }
            
            displayedWinnerIds.add(winnerId);

            Object[] rowData = {
                item.getName(),
                item.getStatus(),
                "$" + item.getStartingPrice(),
                highestBidStr,
                leaderStr
            };
            tableModel.addRow(rowData);
        }
    }

    private void showWinnerDetails() {
        int selectedRow = winnersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int winnerId = displayedWinnerIds.get(selectedRow);
        if (winnerId == -1) {
            JOptionPane.showMessageDialog(this, "There are no bids/winners for this item yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        User winner = userDao.findById(winnerId);
        if (winner != null) {
            String details = "Winner Details:\n\n" +
                             "Name: " + winner.getName() + "\n" +
                             "Email: " + winner.getEmail() + "\n" +
                             "Phone: " + (winner.getPhone() != null ? winner.getPhone() : "N/A") + "\n" +
                             "Address: " + (winner.getAddress() != null ? winner.getAddress() : "N/A");
                             
            JOptionPane.showMessageDialog(this, details, "Winner / Leader Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Could not fetch details for this user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
