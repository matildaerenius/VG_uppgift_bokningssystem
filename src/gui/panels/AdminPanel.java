package gui.panels;

import data.managers.AppointmentManager;
import data.managers.DatabaseManager;
import gui.factories.ImageFactory;
import models.Booking;
import models.TimeFrame;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final JPanel bookingDetails;

    public AdminPanel() {

        Font font = new Font("Times New Roman", Font.BOLD, 20);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 500));

        //Skapar en skalad bild
        ImageIcon scaledIcon = ImageFactory.createScaledImageIcon("src/resources/background.jpg", 400, 500);

        // Lägger den skalade bilden som bakgrunden
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att komponenter kan placeras ovanpå
        add(backgroundLabel);

        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false); // Gör panelen genomskinlig
        backgroundLabel.add(overlayPanel);


        // Rubrik
        JLabel headerLabel = new JLabel("Adminvy", SwingConstants.CENTER);
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setFont(font);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        overlayPanel.add(headerLabel, BorderLayout.NORTH);;

        // Textområdet för att visa bokningar
        bookingDetails = new JPanel();
        bookingDetails.setLayout(new BoxLayout(bookingDetails, BoxLayout.Y_AXIS));
        bookingDetails.setFont(font.deriveFont(Font.PLAIN).deriveFont(14.0f));
        bookingDetails.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(bookingDetails);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        overlayPanel.add(scrollPane, BorderLayout.CENTER);

        // Knapp-panel för admin-funktioner
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton refreshButton = new JButton("Uppdatera Bokningar");
        JButton addTimeButton = new JButton("Lägg till Tid");

        refreshButton.setFont(font.deriveFont(14.0f));
        addTimeButton.setFont(font.deriveFont(14.0f));

        refreshButton.setBackground(Color.WHITE);
        addTimeButton.setBackground(Color.WHITE);

        refreshButton.addActionListener(e -> updateBookingDetails());
        addTimeButton.addActionListener(e -> addAvailableTime());

        buttonPanel.add(refreshButton);
        buttonPanel.add(addTimeButton);
        overlayPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ladda bokningar vid start
        updateBookingDetails();
    }

    private void updateBookingDetails() {
        bookingDetails.removeAll();
        bookingDetails.setLayout(new BoxLayout(bookingDetails, BoxLayout.Y_AXIS));

        var allBookings = DatabaseManager.getInstance().getAllBookings();

        for (Booking booking : allBookings) {
            JPanel bookingPanel = new JPanel();
            bookingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // TODO: sortera bokningarna i ordning


            JLabel bookingLabel = new JLabel(booking.getTimeFrame().getDate() + " | " +
                    booking.getTimeFrame().getStartTime() + " - " +
                    booking.getTimeFrame().getEndTime() + " | " +
                    (booking.isBooked() ? "Bokad av: " + booking.getCustomer().getName() : "Tillgänglig"));
            bookingLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            bookingPanel.add(bookingLabel);

            // Lägger till "Avboka"-knappen endast om bokningen är bokad
            if (booking.isBooked()) {
                JButton cancelButton = new JButton("Avboka");
                cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
                cancelButton.setBackground(Color.WHITE);
                cancelButton.addActionListener(e -> adminCancelTime(booking.getTimeFrame()));
                bookingPanel.add(cancelButton);
            }

            bookingDetails.add(bookingPanel);
        }

        bookingDetails.revalidate();
        bookingDetails.repaint();
    }

    private void adminCancelTime(TimeFrame timeFrame) {
        int choice = JOptionPane.showConfirmDialog(this,
                "Är du säker på att du vill avboka tiden?\n" + timeFrame,
                "Bekräfta avbokning",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = AppointmentManager.getInstance(DatabaseManager.getInstance()).adminCancelAppointment(timeFrame);
            if (success) {
                JOptionPane.showMessageDialog(this, "Bokningen har avbokats.");
            } else {
                JOptionPane.showMessageDialog(this, "Misslyckades med att avboka bokningen.");
            }
            updateBookingDetails(); // Uppdatera vy
        }
    }

    private void addAvailableTime() {
        // Få input från admin
        String date = JOptionPane.showInputDialog("Ange datum (YYYY-MM-DD):");
        String startTime = JOptionPane.showInputDialog("Ange starttid (HH:mm):");
        String endTime = JOptionPane.showInputDialog("Ange sluttid (HH:mm):");

        // Validera input
        if (date == null || startTime == null || endTime == null ||
                date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alla fält måste vara ifyllda.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            TimeFrame timeFrame = new TimeFrame(date, startTime, endTime);
            DatabaseManager.getInstance().createBooking(new Booking(timeFrame, "Available"));
            JOptionPane.showMessageDialog(this, "Tid tillagd: " + date + " " + startTime + " - " + endTime);
            updateBookingDetails(); // Uppdatera adminvyn
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Felaktigt format. Kontrollera datum och tider.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
        }
    }
}

