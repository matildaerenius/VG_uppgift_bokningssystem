package data;

import models.Admin;
import models.Customer;
import models.User;
import data.PasswordUtil;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataManager {
    private static UserDataManager instance;
    private final ConcurrentHashMap<String, User> users;
    private static final String FILE_PATH = "src/resources/userdata.csv";

    private UserDataManager() {
        users = new ConcurrentHashMap<>();
        loadUsersFromFile();
    }

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skippar första raden
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    String phoneNumber = parts[3];
                    String hashedPassword  = parts[4];
                    String userType = parts.length == 6 ? parts[5] : "Customer";

                    User user = UserFactory.createUser(id, name, email, phoneNumber, hashedPassword, userType);
                    users.put(id, user);

                    users.put(id, user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading users from file", e);
        }
    }

    public boolean registerUser(User newUser, String userType) {
        String userId = newUser instanceof Customer ? ((Customer) newUser).getPID() : ((Admin) newUser).getAdminID();
        if (users.containsKey(userId)) {
            return false; // User finns redan med det personnumret
        }

        // Kryptera lösenord
        String hashedPassword = PasswordUtil.hashPassword(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                    userId,
                    newUser.getName(),
                    newUser.getEmail(),
                    newUser.getPhoneNumber(),
                    hashedPassword,
                    userType));
        } catch (IOException e) {
            return false;
        }

        users.put(userId, newUser);
        return true;
    }

    public User authenticateUser(String id, String password) {
        User user = users.get(id);
        return (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) ? user : null;
    }

}
