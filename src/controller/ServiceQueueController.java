package controller;
import model.ServiceBooking;
import service.ServiceQueueService;
import java.util.List;
import java.util.Queue;


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


    public boolean hasWaitingBookings()
    {
        return !service.isEmpty();
    }
}
