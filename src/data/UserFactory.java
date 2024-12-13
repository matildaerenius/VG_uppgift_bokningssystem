package data;

import models.Admin;
import models.Customer;
import models.User;

public class UserFactory {
    public static User createUser(String id, String name, String email, String phoneNumber, String password, String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            return new Admin(id, name, email, phoneNumber, password);
        } else if (userType.equalsIgnoreCase("Customer")) {
            return new Customer(id, name, email, phoneNumber, password);
        } else {
            throw new IllegalArgumentException("Ingen giltig usertype: " + userType);
        }
    }
}

