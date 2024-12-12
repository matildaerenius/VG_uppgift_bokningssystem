package data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    private static final String SALT = "RandomSaltValue";

    // Hashar lösenord med SHA-256 och en salt
    public static String hashPassword(String password) {
        try {
            // Lägg till salt till lösenordet
            String saltedPassword = SALT + password;

            // Skapa en SHA-256 MessageDigest-instans
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generera hash
            byte[] encodedHash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            // Omvandla byte-array till en Base64-sträng för lagring
            return Base64.getEncoder().encodeToString(encodedHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Verifierar lösenordet mot hash
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Hashar det inskrivna lösenordet med samma salt och jämför med den lagrade hashen
        String newHash = hashPassword(plainPassword);
        return newHash.equals(hashedPassword);
    }
}
