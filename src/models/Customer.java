package models;

public class Customer extends User {
    private final String pID;

    public Customer(String id, String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password);
        this.pID = id;
    }

    public String getPID() {
        return pID;
    }
}
