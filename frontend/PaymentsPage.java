package frontend;

import dao.*;
import entities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentsPage extends JPanel {
    private MainFrame frame;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JLabel titleLabel;
    private JButton verifyBtn;

    private final ItemDao itemDao = new ItemDao();
    private final BidDao bidDao = new BidDao();
    private final UserDao userDao = new UserDao();
    private final AuctionDao auctionDao = new AuctionDao();
    private final PaymentDao paymentDao = new PaymentDao();

    // Map rows to payment objects for button logic
    private List<Payment> displayedPayments = new ArrayList<>();

    public PaymentsPage(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        titleLabel = new JLabel("Payments");
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightSection.setBackground(Theme.PRIMARY_COLOR);

        verifyBtn = Theme.createButton("Verify Payment Done", new Color(34, 197, 94)); // Green
        verifyBtn.addActionListener(e -> verifySelectedPayment());
        verifyBtn.setVisible(false); // only visible for Sellers
        rightSection.add(verifyBtn);

        JButton homeBtn = Theme.createButton("Back to Home", Theme.SECONDARY_COLOR);
        homeBtn.addActionListener(e -> frame.navigateTo("HOME"));
        rightSection.add(homeBtn);

        header.add(rightSection, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table initialization placeholder (updated dynamically in refreshData)
        tableModel = new DefaultTableModel();
        paymentTable = new JTable(tableModel);
        paymentTable.setFont(Theme.REGULAR_FONT);
        paymentTable.setRowHeight(30);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Theme.PRIMARY_COLOR);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(Theme.HEADER_FONT);
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        paymentTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Theme.BACKGROUND);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        User currentUser = frame.getCurrentUser();
        if (currentUser == null) return;

        boolean isSeller = "SELLER".equalsIgnoreCase(currentUser.getRole());
        
        // Setup columns based on role
        if (isSeller) {
            titleLabel.setText("Payment Verification (Seller)");
            tableModel.setColumnIdentifiers(new String[]{"Item Name", "Amount", "Buyer Email", "Status"});
            verifyBtn.setVisible(true);
        } else {
            titleLabel.setText("My Purchases (Buyer)");
            tableModel.setColumnIdentifiers(new String[]{"Item Name", "Amount", "Seller Email", "Status"});
            verifyBtn.setVisible(false);
        }

        tableModel.setRowCount(0);
        displayedPayments.clear();

        List<Item> itemsToProcess = new ArrayList<>();
        if (isSeller) {
            itemsToProcess = itemDao.getItemsBySeller(currentUser.getId());
        } else {
            itemsToProcess = itemDao.getAllItems();
        }

        for (Item item : itemsToProcess) {
            AuctionEvent auction = auctionDao.findById(item.getAuctionId());
            if (auction == null || !"COMPLETED".equals(auction.getStatus())) {
                continue; // Only look at completed auctions
            }

            Bid highestBid = bidDao.getHighestBid(item.getId());
            if (highestBid == null) continue; // No winner

            // For buyers, only show items they actually won
            if (!isSeller && highestBid.getBuyerId() != currentUser.getId()) {
                continue;
            }

            // Sync payment record
            Payment payment = paymentDao.getPaymentByItem(item.getId());
            if (payment == null) {
                // Lazily create it
                payment = new Payment(0, highestBid.getBuyerId(), item.getId(), highestBid.getAmount());
                paymentDao.createPayment(payment);
                // Re-fetch to get exact ID and defaults
                payment = paymentDao.getPaymentByItem(item.getId());
                if (payment == null) continue; // failsafe
            }

            displayedPayments.add(payment);

            String contactEmail = "Unknown";
            if (isSeller) {
                User buyer = userDao.findById(payment.getBuyerId());
                if (buyer != null) contactEmail = buyer.getEmail();
            } else {
                User seller = userDao.findById(item.getSellerId());
                if (seller != null) contactEmail = seller.getEmail();
            }

            Object[] row = {
                item.getName(),
                "$" + payment.getAmount(),
                contactEmail,
                payment.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void verifySelectedPayment() {
        int row = paymentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment from the table.", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Payment payment = displayedPayments.get(row);
        if ("SUCCESS".equals(payment.getStatus())) {
            JOptionPane.showMessageDialog(this, "This payment is already verified successful.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you have received $" + payment.getAmount() + " and want to mark this as SUCCESS?",
            "Verify Payment",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean updated = paymentDao.updateStatus(payment.getId(), "SUCCESS");
            if (updated) {
                JOptionPane.showMessageDialog(this, "Payment successfully verified!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
