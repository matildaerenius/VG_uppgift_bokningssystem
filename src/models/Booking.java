package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {

    private final TimeFrame timeFrame;
    private String description;
    private Customer customer;

    public Booking(TimeFrame timeFrame, String description) {
        this.timeFrame = timeFrame;
        this.description = description;
        this.customer = null; // Ledig vid skapandet
    }

    //om man bokar en kund direkt
    public Booking(TimeFrame timeFrame, String description, Customer customer) {
        this.timeFrame = timeFrame;
        this.description = description;
        this.customer = customer; // Bokad direkt
    }

    public boolean isBooked() {
        return customer != null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return timeFrame + ", " + description + ", " + (customer != null ? customer.getName() : "No Customer");
    }
}

