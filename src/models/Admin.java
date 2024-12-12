package models;

public class Admin extends User {
    private final String adminID;

    public Admin(String id, String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password);
        this.adminID = id;
    }

    public String getAdminID() {
        return adminID;
    }
}
