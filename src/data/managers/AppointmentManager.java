package data.managers;

import data.dao.DatabaseDao;
import models.Booking;
import models.Customer;
import models.TimeFrame;

import java.time.LocalDate;
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

    public boolean bookAppointment(Customer customer, String date, String startTime, String endTime) {
        LocalDate localDate = LocalDate.parse(date);
        TimeFrame desiredFrame = new TimeFrame(date, startTime, endTime);

        // Söker efter en redan "Available" bokning med samma datum och tid
        List<Booking> bookings = databaseDao.getAllBookings();
        Booking availableBooking = null;
        for (Booking b : bookings) {
            if (b.getTimeFrame().getDate().equals(localDate)
                    && b.getTimeFrame().getStartTime().equals(desiredFrame.getStartTime())
                    && b.getTimeFrame().getEndTime().equals(desiredFrame.getEndTime())
                    && "Available".equals(b.getDescription())
                    && !b.isBooked()) {
                availableBooking = b;
                break;
            }
        }

        if (availableBooking == null) {
            return false;
        }

        // Kontrollerar om tiden overlappar någon annan bokning
        if (timeFrameOverlaps(localDate, desiredFrame, availableBooking)) {
            return false;
        }

        availableBooking.setCustomer(customer);
        availableBooking.setDescription("Booked");
        databaseDao.updateBookingStatus(availableBooking.getTimeFrame(), customer);
        sendConfirmation(availableBooking);
        return true;
    }



    public boolean cancelAppointment(Customer customer, String date, String startTime) {
        LocalDate localDate = LocalDate.parse(date);
        List<Booking> bookings = databaseDao.getAppointmentsForUser(customer);
        for (Booking booking : bookings) {
            if (booking.getTimeFrame().getDate().equals(localDate)
                    && booking.getTimeFrame().getStartTime().toString().equals(startTime)) {
                booking.setCustomer(null);
                booking.setDescription("Available");
                databaseDao.updateBookingStatus(booking.getTimeFrame(), null);
                return true;
            }
        }
        return false;
    }

    private boolean timeFrameOverlaps(LocalDate date, TimeFrame newTimeFrame, Booking currentBooking) {
        List<Booking> bookings = databaseDao.getAllBookings();
        for (Booking booking : bookings) {
            if (booking == currentBooking) continue;

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

    // TODO: Kan tas bort, skrivs bara ut i terminalen
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


