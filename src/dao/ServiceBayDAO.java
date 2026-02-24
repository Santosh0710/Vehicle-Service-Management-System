package dao;

import model.ServiceBay;
import model.BayStatus;
import jdbc.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceBayDAO
{
//    Get First Available Bay
    public ServiceBay getAvailableBay() throws SQLException
    {
        String sql = "SELECT * FROM service_bay WHERE status = 'AVAILABLE' LIMIT 1";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToBay(rs);
            }
        }

        return null; // No available bay
    }
//    Update Bay Status
    public void updateBayStatus(int bayId , BayStatus status) throws SQLException
    {
        String sql = "UPDATE service_bay SET status = ? WHERE bay_id = ?";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, bayId);

            stmt.executeUpdate();
        }
    }
    // Get all bays (for UI display later)
    public List<ServiceBay> getAllBays() throws SQLException {

        String sql = "SELECT * FROM service_bay";

        List<ServiceBay> bays = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bays.add(mapResultSetToBay(rs));
            }
        }

        return bays;
    }

    // Mapping method
    private ServiceBay mapResultSetToBay(ResultSet rs) throws SQLException {

        int bayId = rs.getInt("bay_id");
        String bayName = rs.getString("bay_name");
        BayStatus status = BayStatus.valueOf(rs.getString("status"));

        return new ServiceBay(bayId, bayName, status);
    }
}
