package gui;

import data.AppointmentManager;
import data.DatabaseManager;
import data.UserDataManager;
import models.Admin;
import models.Customer;
import models.User;

import javax.swing.*;
import java.awt.*;

public class ViewManager extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final AppointmentManager appointmentManager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewManager::new);
    }

    public ViewManager() {
        setTitle("Bokningssystem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
//        setResizable(false);
        setFocusable(false);

        DatabaseManager databaseManager = DatabaseManager.getInstance();
        UserDataManager userDataManager = UserDataManager.getInstance();
        appointmentManager = AppointmentManager.getInstance(databaseManager);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Lägger till de olika windowsen
        mainPanel.add(new StartPanel(this), "Start");
        mainPanel.add(new LoginPanel(this, userDataManager), "Login");
        mainPanel.add(new RegistrationPanel(this), "Register");
        mainPanel.add(new AdminPanel(), "Admin");

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }

    public void showAdminPanel() {
        showCard("Admin");
    }

    public void showBookingPanel(User user) {
        // Kontrollera om användaren är kund eller admin
        boolean isAdmin = user instanceof Admin;
        if (isAdmin) {
            showAdminPanel();
        } else {
            BookingPanel bookingPanel = new BookingPanel(user.getEmail(), AppointmentManager.getInstance(DatabaseManager.getInstance()), (Customer) user);
            mainPanel.add(bookingPanel, "Booking");
            showCard("Booking");
        }
    }
}

