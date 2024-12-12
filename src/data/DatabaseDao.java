package data;

import models.Booking;
import models.Customer;
import models.TimeFrame;

import java.util.List;

public interface DatabaseDao {

    void createBooking(Booking booking);

    void deleteBooking(Booking booking);

    List<Booking> getAllBookings();

    List<Booking> getAppointmentsForUser(Customer customer);

    void updateBookingStatus(TimeFrame timeFrame, Customer customer);
}
