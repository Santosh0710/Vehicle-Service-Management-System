package dao;

import jdbc.DbConnectionUtil;
import model.ServiceBooking;
import model.ServiceType;
import model.ServiceStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceBookingDAO
{
//    --------
//    Add Booking
//    --------
    public void addBooking(ServiceBooking booking) throws SQLException
    {
        String sql = "INSERT INTO service_booking (booking_id , vehicle_id , service_type , status , booking_date ) VALUES (? ,? ,? ,?, ?)";
//        this is called try with resources , after block finishes , connection and statement auto close
        try(Connection con  = DbConnectionUtil.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);)
        {
            stmt.setInt(1 , booking.getBookingId());
            stmt.setInt(2 , booking.getVehicleId());
//            ServiceType is enum , but database stores Strings , not enum objects.
//            that is why .name() is used.
            stmt.setString(3 , booking.getServiceType().name());
            stmt.setString(4 , booking.getStatus().name());

            stmt.setDate(5, Date.valueOf(booking.getBookingDate()));
//          INSERT / UPDATE / DELETE → use executeUpdate().
//          SELECT → use executeQuery().
            stmt.executeUpdate();
        }
    }

//    Update Booking Function
public boolean updateBooking(ServiceBooking booking) throws SQLException{
    String query = "UPDATE service_booking SET vehicle_id=?, service_type=?, booking_date=? WHERE booking_id=?";

    try (Connection conn = DbConnectionUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, booking.getVehicleId());
        ps.setString(2, booking.getServiceType().name());
        ps.setDate(3, java.sql.Date.valueOf(booking.getBookingDate()));
        ps.setInt(4, booking.getBookingId());

        int rows = ps.executeUpdate();
        return rows > 0;

    }
}
// Method for Deleting the booking
public boolean deleteBooking(int bookingId) throws SQLException {

    String sql = "DELETE FROM service_booking WHERE booking_id = ?";

    try (Connection conn = DbConnectionUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, bookingId);

        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
// --------------
//    Check Duplicate Booking ID
// --------------
    public boolean existsByBookingId(int bookingId) throws SQLException
    {
//
        String sql = "SELECT 1 FROM service_booking WHERE booking_id = ?";
        try(Connection con = DbConnectionUtil.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);)
        {
            stmt.setInt(1 , bookingId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();  // true if exists
        }

    }
//    ----------
//    Check if vehicle already has active booking
//    ----------
    public boolean existsActiveBookingByVehicleId(int vehicleId) throws SQLException
    {
        String sql = """
                SELECT 1 FROM service_booking 
                WHERE vehicle_id = ? 
                AND (status = 'WAITING' OR status = 'IN_PROGRESS')
                """;

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, vehicleId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    // ----------------------------
    // Get current IN_PROGRESS booking
    // ----------------------------
    public ServiceBooking getCurrentInProgressBooking() throws SQLException {

        String sql = "SELECT * FROM service_booking WHERE status = 'IN_PROGRESS'";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        }

        return null;
    }
    // ----------------------------
    // Get next WAITING booking
    // (FIFO using booking_id order)
    // ----------------------------
    public ServiceBooking getNextWaitingBooking() throws SQLException {

        String sql = """
                SELECT * FROM service_booking 
                WHERE status = 'WAITING'
                ORDER BY booking_id ASC
                LIMIT 1
                """;

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        }

        return null;
    }
    // ----------------------------
    // Update Status
    // ----------------------------
    public void updateStatusAndBay(int bookingId,
                                   ServiceStatus status,
                                   Integer bayId) throws SQLException {

        String sql = "UPDATE service_booking SET status = ?, bay_id = ? WHERE booking_id = ?";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            if (bayId != null) {
                stmt.setInt(2, bayId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setInt(3, bookingId);

            stmt.executeUpdate();
        }
    }

    // ----------------------------
    // Get bookings by status
    // ----------------------------
    public List<ServiceBooking> getBookingsByStatus(ServiceStatus status) throws SQLException {

        String sql = "SELECT * FROM service_booking WHERE status = ? ORDER BY booking_id ASC";

        List<ServiceBooking> bookings = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }

        return bookings;
    }
//     Get the booking with the help of bookingId
    public ServiceBooking getBookingById(int bookingId) throws SQLException {

        String sql = "SELECT * FROM service_booking WHERE booking_id = ?";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        }

        return null;
    }
//    Method to get all the bookings.
public List<ServiceBooking> getAllBookings() throws SQLException {

    String sql = "SELECT * FROM service_booking ORDER BY booking_id ASC";

    List<ServiceBooking> bookings = new ArrayList<>();

    try (Connection conn = DbConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            bookings.add(mapResultSetToBooking(rs));
        }
    }

    return bookings;
}

    // ----------------------------
    // Helper Method
    // ----------------------------
    private ServiceBooking mapResultSetToBooking(ResultSet rs) throws SQLException {

        int bookingId = rs.getInt("booking_id");
        int vehicleId = rs.getInt("vehicle_id");

        ServiceType serviceType =
                ServiceType.valueOf(rs.getString("service_type"));

        ServiceStatus status =
                ServiceStatus.valueOf(rs.getString("status"));

        Integer bayId = rs.getObject("bay_id") != null
                ? rs.getInt("bay_id")
                : null;

        Date sqlDate = rs.getDate("booking_date");
        java.time.LocalDate bookingDate = (sqlDate != null)
                ? sqlDate.toLocalDate()
                : null;

        return new ServiceBooking(
                bookingId,
                vehicleId,
                serviceType,
                status,
                bayId,
                bookingDate
        );
    }

}