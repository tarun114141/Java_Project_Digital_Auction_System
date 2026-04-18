package frontend;

import dao.ItemDao;
import dao.UserDao;
import entities.Item;
import entities.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPage extends JPanel {
    private MainFrame frame;
    private JTable usersTable;
    private JTable itemsTable;
    private DefaultTableModel usersTableModel;
    private DefaultTableModel itemsTableModel;

    private final UserDao userDao = new UserDao();
    private final ItemDao itemDao = new ItemDao();

    private List<Item> allItems;

    private JLabel totalUsersLabel;
    private JLabel totalItemsLabel;
    private JLabel totalRevenueLabel;

    public AdminPage(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        // Header Buttons
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightSection.setBackground(Theme.PRIMARY_COLOR);

        JButton logoutBtn = Theme.createButton("Logout", new Color(220, 38, 38));
        logoutBtn.addActionListener(e -> {
            frame.setCurrentUser(null);
            frame.navigateTo("LOGIN");
        });
        rightSection.add(logoutBtn);

        header.add(rightSection, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.HEADER_FONT);

        // --- USERS TAB ---
        JPanel usersPanel = new JPanel(new BorderLayout());
        String[] userCols = {"User ID", "Name", "Email", "Role", "Phone"};
        usersTableModel = new DefaultTableModel(userCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        usersTable = createStyledTable(usersTableModel);
        JScrollPane usersScroll = new JScrollPane(usersTable);
        usersScroll.setBorder(new EmptyBorder(20, 20, 20, 20));
        usersPanel.add(usersScroll, BorderLayout.CENTER);
        tabbedPane.addTab("Manage Users", usersPanel);

        // --- ITEMS TAB ---
        JPanel itemsPanel = new JPanel(new BorderLayout());
        String[] itemCols = {"Item ID", "Name", "Starting Price", "Status", "Seller ID"};
        itemsTableModel = new DefaultTableModel(itemCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        itemsTable = createStyledTable(itemsTableModel);
        JScrollPane itemsScroll = new JScrollPane(itemsTable);
        itemsScroll.setBorder(new EmptyBorder(20, 20, 20, 20));
        itemsPanel.add(itemsScroll, BorderLayout.CENTER);

        JPanel itemsBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        itemsBottomPanel.setBackground(Theme.BACKGROUND);
        JButton deleteItemBtn = Theme.createButton("Delete Selected Item", new Color(220, 38, 38));
        deleteItemBtn.addActionListener(e -> deleteSelectedItem());
        itemsBottomPanel.add(deleteItemBtn);
        itemsPanel.add(itemsBottomPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Items", itemsPanel);

        // --- DASHBOARD METRICS ---
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        metricsPanel.setBackground(Theme.BACKGROUND);
        metricsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        totalUsersLabel = createMetricCard(metricsPanel, "Total Users", "0");
        totalItemsLabel = createMetricCard(metricsPanel, "Total Items", "0");
        totalRevenueLabel = createMetricCard(metricsPanel, "Total Auction Value", "$0");

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(metricsPanel, BorderLayout.NORTH);
        centerWrapper.add(tabbedPane, BorderLayout.CENTER);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JLabel createMetricCard(JPanel parent, String titleText, String valueText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel title = new JLabel(titleText);
        title.setFont(Theme.REGULAR_FONT);
        title.setForeground(Theme.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel value = new JLabel(valueText);
        value.setFont(Theme.TITLE_FONT);
        value.setForeground(Theme.PRIMARY_COLOR);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(value);

        parent.add(card);
        return value;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(Theme.REGULAR_FONT);
        table.setRowHeight(30);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Theme.PRIMARY_COLOR);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(Theme.HEADER_FONT);
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        return table;
    }

    public void refreshData() {
        // Refresh Users
        usersTableModel.setRowCount(0);
        List<User> users = userDao.getAllUsers();
        for (User u : users) {
            usersTableModel.addRow(new Object[]{
                u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getPhone()
            });
        }

        // Refresh Items & Dashboard
        itemsTableModel.setRowCount(0);
        allItems = itemDao.getAllItems();
        double totalValue = 0;
        
        for (Item i : allItems) {
            itemsTableModel.addRow(new Object[]{
                i.getId(), i.getName(), "$" + i.getStartingPrice(), i.getStatus(), i.getSellerId()
            });
            totalValue += i.getStartingPrice();
        }

        totalUsersLabel.setText(String.valueOf(users.size()));
        totalItemsLabel.setText(String.valueOf(allItems.size()));
        totalRevenueLabel.setText("$" + String.format("%.2f", totalValue));
    }

    private void deleteSelectedItem() {
        int row = itemsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Item item = allItems.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to FORCE DELETE item '" + item.getName() + "'?\nWarning: This may fail if there are active bids tied to it.",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (itemDao.deleteItem(item.getId())) {
                JOptionPane.showMessageDialog(this, "Item successfully deleted.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete item. It likely has associated bids or payments blocking deletion.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
