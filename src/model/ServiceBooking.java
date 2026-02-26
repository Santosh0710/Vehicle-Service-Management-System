package model;

import java.time.LocalDate;

public class ServiceBooking
{
    private int bookingId;
    private int vehicleId;
    private ServiceType serviceType;
    private ServiceStatus status;

//    Why Integer and not int? Because: //WAITING → bayId = null ,IN_PROGRESS → bayId = some value
//COMPLETED → still assigned ,Primitive int cannot store null.
    private Integer bayId;
    private LocalDate bookingDate;

    public ServiceBooking(int bookingId, int vehicleId , ServiceType serviceType)
    {
        this.bookingId =bookingId;
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.status = ServiceStatus.WAITING;
    }
    public ServiceBooking(int bookingId,
                          int vehicleId,
                          ServiceType serviceType,
                          ServiceStatus status,
                          Integer bayId,
                          LocalDate bookingDate)
    {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.status = status;
        this.bayId = bayId;
        this.bookingDate = bookingDate;
    }

    public int getBookingId()
    {
        return bookingId;
    }
    public int getVehicleId()
    {
        return vehicleId;
    }
    public ServiceType getServiceType()
    {
        return serviceType;
    }
    public ServiceStatus getStatus()
    {
        return status;
    }
    public void setStatus(ServiceStatus status)
    {
        this.status = status;
    }

    public Integer getBayId()
    {
        return bayId;
    }
    public void setBayId(Integer bayId)
    {
        this.bayId = bayId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

//    very important for GUI Display
@Override
public String toString() {
    return "Booking Id: " + bookingId +
            ", Vehicle Id: " + vehicleId +
            ", Service: " + serviceType +
            ", Status: " + status +
            ", Bay Id: " + (bayId != null ? bayId : "Not Assigned") +
            ", Booking Date: " + bookingDate;
}
}
