package dao;

import model.Vehicle;
import exception.DatabaseException;
import jdbc.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // ================= ADD =================
    public int addVehicle(Vehicle vehicle) {

        String sql = "INSERT INTO vehicles (customer_id, vehicle_number, vehicle_type, brand, model, year) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getVehicleNumber());
            ps.setString(3, vehicle.getVehicleType());
            ps.setString(4, vehicle.getBrand());
            ps.setString(5, vehicle.getModel());
            ps.setInt(6, vehicle.getYear());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new DatabaseException("Failed to insert vehicle", null);

        } catch (SQLException e) {
            e.printStackTrace(); // 👈 debugging
            throw new DatabaseException("Error adding vehicle", e);
        }
    }
    // ================= UPDATE =================
    public void updateVehicle(Vehicle vehicle) {

        String sql = "UPDATE vehicles SET customer_id = ?, vehicle_number = ?, vehicle_type = ?, brand = ?, model = ?, year = ? WHERE vehicle_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getVehicleNumber());
            ps.setString(3, vehicle.getVehicleType());
            ps.setString(4, vehicle.getBrand());
            ps.setString(5, vehicle.getModel());
            ps.setInt(6, vehicle.getYear());
            ps.setInt(7, vehicle.getVehicleId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException("Vehicle not found for update", null);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error updating vehicle", e);
        }
    }

    // ================= DELETE =================
    public void deleteVehicle(int vehicleId) {

        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, vehicleId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException("Vehicle not found for deletion");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting vehicle", e);
        }
    }

    // ================= GET BY ID =================
    public Vehicle getVehicleById(int vehicleId) {

        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToVehicle(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching vehicle", e);
        }
    }
// GET VEHICLES BY CUSTOMER_ID
    public List<Vehicle> getVehiclesByCustomerId(int customerId)
    {

        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles WHERE customer_id = ?";

            try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
                ) {

                ps.setInt(1, customerId);

                ResultSet rs = ps.executeQuery();

                while (rs.next())
                {
                    vehicles.add(mapRowToVehicle(rs));
                }

                  } catch (SQLException e)
                {
                    throw new DatabaseException("Error fetching vehicles by customer ID", e);
                }

                return vehicles;
    }

    // ================= GET ALL =================
    public List<Vehicle> getAllVehicles() {

        List<Vehicle> list = new ArrayList<>();

        String sql = "SELECT * FROM vehicles";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching vehicles", e);
        }

        return list;
    }

    // ================= PAGINATION =================
    public List<Vehicle> getVehiclesPaginated(int pageNumber, int pageSize) {

        List<Vehicle> list = new ArrayList<>();

        String sql = "SELECT * FROM vehicles LIMIT ? OFFSET ?";

        int offset = (pageNumber - 1) * pageSize;

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching paginated vehicles", e);
        }

        return list;
    }

    public int getTotalVehicleCount() {

        String sql = "SELECT COUNT(*) FROM vehicles";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error counting vehicles", e);
        }
    }

    // ================= EXISTS =================
    public boolean existsById(int vehicleId) {

        String sql = "SELECT 1 FROM vehicles WHERE vehicle_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new DatabaseException("Error checking vehicle existence", e);
        }
    }
    //  method to check vehicle through number
    public boolean existsByVehicleNumber(String vehicleNumber) {

        String sql = "SELECT 1 FROM vehicles WHERE vehicle_number = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, vehicleNumber);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new DatabaseException("Error checking vehicle number existence", e);
        }
    }
    // ================= MAPPER =================
    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {

        int vehicleId = rs.getInt("vehicle_id");
        int customerId = rs.getInt("customer_id");
        String vehicleNumber = rs.getString("vehicle_number");
        String vehicleType = rs.getString("vehicle_type");
        String brand = rs.getString("brand");
        String model = rs.getString("model");
        int year = rs.getInt("year");

        return new Vehicle(
                vehicleId,
                customerId,
                vehicleNumber,
                vehicleType,
                brand,
                model,
                year
        );
    }
}