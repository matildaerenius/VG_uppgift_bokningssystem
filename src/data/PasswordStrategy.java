package data;

public interface PasswordStrategy {
    String hashPassword(String password);
    boolean verifyPassword(String plainPassword, String hashedPassword);
}

