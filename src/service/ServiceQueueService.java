package service;

import dao.ServiceBookingDAO;
import dao.ServiceBayDAO;
import model.ServiceBooking;
import model.ServiceStatus;
import model.ServiceBay;
import model.BayStatus;

import exception.DuplicateBookingException;
import exception.VehicleAlreadyBookedException;
import exception.DatabaseException;
import exception.BookingNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class ServiceQueueService {

    private final ServiceBookingDAO bookingDAO;
    private final ServiceBayDAO serviceBayDAO;


    public ServiceQueueService(ServiceBookingDAO bookingDAO,
                               ServiceBayDAO serviceBayDAO) {
        this.bookingDAO = bookingDAO;
        this.serviceBayDAO = serviceBayDAO;
    }


    // -------------------------
    // Add Booking (DB Based)
    // -------------------------
    public void addBooking(ServiceBooking booking) {

        try {
            // Check duplicate booking ID
            if (bookingDAO.existsByBookingId(booking.getBookingId())) {
                throw new DuplicateBookingException(
                        "Duplicate Booking ID not allowed."
                );
            }

            // Check active vehicle booking (WAITING or IN_PROGRESS)
            if (bookingDAO.existsActiveBookingByVehicleId(booking.getVehicleId())) {
                throw new VehicleAlreadyBookedException(
                        "This vehicle already has an active booking."
                );
            }

            // Set initial status
            booking.setStatus(ServiceStatus.WAITING);

            bookingDAO.addBooking(booking);

        } catch (SQLException e) {
            throw new DatabaseException("Database error while adding booking.");
        }
    }
// method for Updating bookings
public void updateBooking(ServiceBooking booking) {
    try {
        boolean updated = bookingDAO.updateBooking(booking);

        if (!updated) {
            throw new BookingNotFoundException(
                    "Booking ID not found: " + booking.getBookingId()
            );
        }

    } catch (SQLException e) {
        throw new DatabaseException("Database error while updating booking", e);
    }
}
// Method for deleting the booking

    public void deleteBooking(int bookingId) {

        try {
            boolean deleted = bookingDAO.deleteBooking(bookingId);

            if (!deleted) {
                throw new BookingNotFoundException(
                        "Booking ID not found: " + bookingId
                );
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting booking", e);
        }
    }

    // -------------------------
    // Serve Next (DB Based)
    // -------------------------
    public ServiceBooking serveNext() {

        try {

            // Getting  available bay First
            ServiceBay availableBay = serviceBayDAO.getAvailableBay();

            if (availableBay == null) {
                return null; // No free bays
            }

            // now Getting next waiting booking
            ServiceBooking nextBooking = bookingDAO.getNextWaitingBooking();

            if (nextBooking == null) {
                return null; // No customers waiting
            }

            // 3⃣ Update booking → IN_PROGRESS + assign bay
            bookingDAO.updateStatusAndBay(
                    nextBooking.getBookingId(),
                    ServiceStatus.IN_PROGRESS,
                    availableBay.getBayId()
            );

            // 4 Update bay → OCCUPIED
            serviceBayDAO.updateBayStatus(
                    availableBay.getBayId(),
                    BayStatus.OCCUPIED
            );

            // Update in-memory object for UI consistency
            nextBooking.setStatus(ServiceStatus.IN_PROGRESS);
            nextBooking.setBayId(availableBay.getBayId());

            return nextBooking;

        } catch (SQLException e) {
            throw new RuntimeException("Error while serving next booking", e);
        }
    }


    // -------------------------
    // Complete the booking on a particular bay
    // -------------------------
    public ServiceBooking completeService(int bookingId) {

        try {

            //  Get booking from DB
            ServiceBooking booking = bookingDAO.getBookingById(bookingId);

            if (booking == null) {
                throw new RuntimeException("Booking not found");
            }

            if (booking.getStatus() != ServiceStatus.IN_PROGRESS) {
                throw new RuntimeException("Booking is not in progress");
            }

            int bayId = booking.getBayId();

            //  Update booking → COMPLETED
            bookingDAO.updateStatusAndBay(
                    bookingId,
                    ServiceStatus.COMPLETED,
                    bayId
            );

            //  Update bay → AVAILABLE
            serviceBayDAO.updateBayStatus(
                    bayId,
                    BayStatus.AVAILABLE
            );
//            Updating the object for UI
            booking.setStatus(ServiceStatus.COMPLETED);

            return booking;

        } catch (SQLException e) {
            throw new RuntimeException("Error completing service", e);
        }
    }


    // -------------------------
    // Get Waiting Bookings
    // -------------------------
    public List<ServiceBooking> getPendingBookings() {
        try {
            return bookingDAO.getBookingsByStatus(ServiceStatus.WAITING);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching pending bookings.");
        }
    }

    // -------------------------
    // Get Completed Bookings
    // -------------------------
    public List<ServiceBooking> getCompletedBookings() {
        try {
            return bookingDAO.getBookingsByStatus(ServiceStatus.COMPLETED);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching completed bookings.");
        }
    }

    // -------------------------
    // Get In Progress Bookings
    // -------------------------
    public List<ServiceBooking> getInProgressBookings() {
        try {
            return bookingDAO.getBookingsByStatus(ServiceStatus.IN_PROGRESS);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching in-progress bookings", e);
        }
    }
// Getting All the bookings
public List<ServiceBooking> getAllBookings() {
    try {
        return bookingDAO.getAllBookings();
    } catch (SQLException e) {
        throw new DatabaseException("Error fetching all bookings");
    }
}

    public boolean isEmpty() {
        try {
            return bookingDAO.getBookingsByStatus(ServiceStatus.WAITING).isEmpty();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while checking queue.");
        }
    }
}
