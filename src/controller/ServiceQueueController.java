package controller;
import model.ServiceBooking;
import service.ServiceQueueService;
import java.util.List;
import java.time.LocalDate;


public class ServiceQueueController
{
    private final ServiceQueueService service;

    public ServiceQueueController(ServiceQueueService service)
    {
        this.service = service;
    }
    public void addBooking(ServiceBooking booking)
    {
        service.addBooking(booking);
    }
    public void updateBooking(ServiceBooking booking)
    {
        service.updateBooking(booking);
    }
    public void deleteBooking(int bookingId) {
        service.deleteBooking(bookingId);
    }

    public ServiceBooking serveNext()
    {
        return service.serveNext();
    }

    public ServiceBooking completeService(int bookingId)
    {
        return service.completeService(bookingId);
    }


    public List<ServiceBooking> getPendingBookings()
    {
        return service.getPendingBookings();
    }

    public List<ServiceBooking> getCompletedBookings()
    {
        return service.getCompletedBookings();
    }

    public List<ServiceBooking> getInProgressBookings() {
        return service.getInProgressBookings();
    }
    public List<ServiceBooking> getAllBookings()
    {
        return service.getAllBookings();
    }
    public List<ServiceBooking> getBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return service.getBookingsBetweenDates(startDate, endDate);
    }


    public boolean hasWaitingBookings()
    {
        return !service.isEmpty();
    }


    //    Methods for Managing DashBoard Cards


    public int getTodayBookingsCount() {
        return service.getTodayBookingsCount();
    }

    public int getTodayCompletedCount() {
        return service.getTodayCompletedCount();
    }

    public int getTodayInProgressBookingsCount() {
        return service.getTodayInProgressBookingsCount();
    }

    public int getTodayWaitingCount() {
        return service.getTodayWaitingCount();
    }

}
