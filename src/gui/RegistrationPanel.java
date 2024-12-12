package gui;

import data.UserDataManager;
import models.Customer;
import models.User;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {

    public RegistrationPanel(ViewManager parentFrame) {

        Font font = new Font("Times New Roman", Font.BOLD, 16); //Används för alla typsnitt, ändra med .deriveFont()

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 550));

        //Skapar en skalad bild
        ImageIcon scaledIcon = ImageFactory.createScaledImageIcon("src/resources/background.jpg", 400, 500);

        // Lägger den skalade bilden som bakgrunden
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att komponenter kan placeras ovanpå
        add(backgroundLabel);

        // Panel för registreringsfält
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false); // Gör panelen genomskinlig
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Array med namn på knapparna
        String[] labelNames = new String[]{
                "Förnamn:",
                "Efternamn:",
                "Personnummer (yymmddxxxx):",
                "Telefonnummer:",
                "Epostadress:"
        };

        final int FIRSTNAME = 0;
        final int LASTNAME = 1;
        final int PERSON_NR = 2;
        final int TELEFON_NR = 3;
        final int EPOST = 4;

        JLabel[] labels = new JLabel[labelNames.length]; //Arrays för att spara labels och fields
        JTextField[] fields = new JTextField[labelNames.length];

        //loopar igenom labelNames
        for (int i = 0; i < labelNames.length; i++) {
            labels[i] = new JLabel(labelNames[i]); //label med texten som är i labelNames på det indexet
            labels[i].setFont(font);
            fieldPanel.add(labels[i]); //lägger till i fieldPanel
            fields[i] = new JTextField();
            fields[i].setFont(font.deriveFont(14.0f).deriveFont(Font.PLAIN));
            fieldPanel.add(fields[i]); //lägger till i fieldPanel
            fieldPanel.add(Box.createVerticalStrut(10)); //Box för mellanrum
        }

        JLabel passwordLabel = new JLabel("Lösenord:");
        passwordLabel.setFont(font);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font.deriveFont(14.0f));

        // Checkbox för att visa/dölja lösenord
        JCheckBox showPasswordCheckBox = new JCheckBox("Visa lösenord");
        showPasswordCheckBox.setOpaque(false); // Gör checkboxen genomskinlig
        showPasswordCheckBox.setFont(font.deriveFont(14.0f));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Visar texten i lösenordsfältet
            } else {
                passwordField.setEchoChar('*'); // Döljer texten med stjärnor
            }
        });

        // Lägger till komponenter till fieldPanel
        fieldPanel.add(passwordLabel);
        fieldPanel.add(passwordField);
        fieldPanel.add(Box.createVerticalStrut(10));
        fieldPanel.add(showPasswordCheckBox);

        backgroundLabel.add(fieldPanel, BorderLayout.CENTER);

        // Panel för knappar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Gör panelen genomskinlig
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton registerButton = new JButton("Registrera dig");
        registerButton.setFont(font);
        registerButton.setPreferredSize(new Dimension(150, 30));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(Color.BLACK);

        JButton backButton = new JButton("Tillbaka");
        backButton.setFont(font.deriveFont(Font.BOLD));
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);

        // ActionListeners för knappar
        registerButton.addActionListener(e -> {
            String firstName = fields[FIRSTNAME].getText();
            String lastName = fields[LASTNAME].getText();
            String id = fields[PERSON_NR].getText();
            String phonenumber = fields[TELEFON_NR].getText();
            String email = fields[EPOST].getText();
            String password = new String(passwordField.getPassword());

            // Kontrollerar att alla fält är ifyllda
            if (firstName.isEmpty() || lastName.isEmpty() || id.isEmpty() ||
                    phonenumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alla fält måste vara ifyllda.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // TODO: alla nedan kontroller kan nog göras med regex istället, kanske inte behövs ändras men
            // Kontrollerar att persnr består av siffror och endast 10st
            if (id.length() != 10 || !id.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(this, "Ange personnummer i tio siffror", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Kontrollerar att telnr består av siffror och endast 10st
            if (phonenumber.length() != 10 || !phonenumber.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(this, "Ogiltigt telefonnummer", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Kontrollerar om e-postadressen innehåller @
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Ogiltig e-postadress", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Sparar till fil
            User newUser = new Customer(id, firstName + " " + lastName, email, phonenumber, password);
            boolean success = UserDataManager.getInstance().registerUser(newUser, "Customer");
            if (success) {
                JOptionPane.showMessageDialog(this, "Registrering lyckades!");
                parentFrame.showCard("Login");
            } else {
                JOptionPane.showMessageDialog(this, "Användare med detta personnummer finns redan.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> parentFrame.showCard("Start"));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);
    }
}
