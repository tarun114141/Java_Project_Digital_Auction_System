package frontend;

import com.auction.services.AuctionSystem;
import com.auction.entities.User;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {
    private JTextField emailField;
    private JPasswordField passField;

    public LoginPage(MainFrame frame, AuctionSystem system) {
        setBackground(Theme.BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel loginBox = new JPanel();
        loginBox.setBackground(Theme.CARD_BG);
        loginBox.setPreferredSize(new Dimension(400, 350));
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 40));
        emailField.setFont(Theme.REGULAR_FONT);

        passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(300, 40));
        passField.setFont(Theme.REGULAR_FONT);

        JButton loginBtn = Theme.createButton("Login", Theme.PRIMARY_COLOR);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String pwd = new String(passField.getPassword());
            if (system.login(email, pwd)) {
                // Determine user (hacky way since AuctionSystem login returns boolean only currently)
                // In real app, system.login() should return User
                User loggedIn = new User(1, email, email, pwd); 
                frame.setCurrentUser(loggedIn);
                frame.navigateTo("HOME");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginBox.add(titleLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        loginBox.add(new JLabel("Email"));
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(emailField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));
        loginBox.add(new JLabel("Password"));
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(passField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        loginBox.add(loginBtn);

        add(loginBox);
    }
}
