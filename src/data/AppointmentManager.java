package data;

import models.Booking;
import models.Customer;
import models.TimeFrame;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentManager {

    private static AppointmentManager instance;
    private final DatabaseDao databaseDao;

    public AppointmentManager(DatabaseDao dataBaseDao) {
        this.databaseDao = dataBaseDao;
    }

    public static synchronized AppointmentManager getInstance(DatabaseDao databaseDao) {
        if (instance == null) {
            instance = new AppointmentManager(databaseDao);
        }
        return instance;
    }

    // TODO: Denna används inte utan görs direkt i databasemanager, ta bort eller gör om så den används här istället
    // TODO: Uppdatera BookingPanel att använda AppointmentManager.bookAppointment() istället för att direkt göra med DatabaseManager
    public boolean bookAppointment(Customer customer, String date, String startTime, String endTime) {
        TimeFrame timeFrame = new TimeFrame(date, startTime, endTime);
        Booking booking = new Booking(timeFrame, "Booked", customer);

        if (!timeFrameOverlaps(LocalDate.parse(date), timeFrame)) {
            databaseDao.createBooking(booking);
            sendConfirmation(booking);
            return true;
        }
        return false;
    }

    // TODO: gör också så denna används inte dirre i databasemanager
    public boolean cancelAppointment(Booking b) {
        List<Booking> bookings = databaseDao.getAppointmentsForUser(b.getCustomer());
        for (Booking booking : bookings) {
            if (booking.getTimeFrame().equals(b.getTimeFrame())) {
                // Gör bokningen tillgänglig
                booking.setCustomer(null);
                booking.setDescription("Available");
                databaseDao.updateBookingStatus(booking.getTimeFrame(), null);
                return true;
            }
        }
        return false;
    }

    private boolean timeFrameOverlaps(LocalDate date, TimeFrame newTimeFrame) {
        List<Booking> bookings = databaseDao.getAllBookings();
        for (Booking booking : bookings) {
            if (booking.getTimeFrame().getDate().equals(date)) {
                TimeFrame existingFrame = booking.getTimeFrame();
                if (existingFrame.getEndTime().isAfter(newTimeFrame.getStartTime()) &&
                        existingFrame.getStartTime().isBefore(newTimeFrame.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: Kan tas bort, skrivs bara ut i terminalen och tas bookappointment bort behövs inte denna heller
    private void sendConfirmation(Booking booking) {
        System.out.println("Confirmation sent for booking: " + booking.getTimeFrame());
    }

    public List<Booking> getAppointmentsForUser(Customer customer) {
        return DatabaseManager.getInstance().getAppointmentsForUser(customer);
    }

    public boolean adminCancelAppointment(TimeFrame timeFrame) {
        List<Booking> bookings = databaseDao.getAllBookings();
        for (Booking booking : bookings) {
            if (booking.getTimeFrame().equals(timeFrame) && booking.isBooked()) {
                // Gör bokningen tillgänglig
                booking.setCustomer(null);
                booking.setDescription("Available");
                databaseDao.updateBookingStatus(timeFrame, null);
                return true;
            }
        }
        return false; // Ingen matchande bokning hittades
    }
}


