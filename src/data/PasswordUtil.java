package data;

    public class PasswordUtil {
        private PasswordStrategy strategy;

        public PasswordUtil(PasswordStrategy strategy) {
            this.strategy = strategy;
        }

        public String hashPassword(String password) {
            return strategy.hashPassword(password);
        }

        public boolean verifyPassword(String plainPassword, String hashedPassword) {
            return strategy.verifyPassword(plainPassword, hashedPassword);
        }
    }
