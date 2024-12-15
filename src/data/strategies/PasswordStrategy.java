package data.strategies;

public interface PasswordStrategy {
    String hashPassword(String password);
    boolean verifyPassword(String plainPassword, String hashedPassword);
}

