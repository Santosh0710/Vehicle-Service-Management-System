package service;

import dao.VehicleDAO;
import exception.BusinessException;
import exception.DatabaseException;
import model.Vehicle;

import java.util.List;

public class VehicleService {

    private VehicleDAO vehicleDAO = new VehicleDAO();

    // ================= ADD =================
    public int addVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        // check duplicate vehicle number
        if (vehicleDAO.existsByVehicleNumber(vehicle.getVehicleNumber())) {
            throw new BusinessException("Vehicle number already exists");
        }

        return vehicleDAO.addVehicle(vehicle);
    }

    // ================= UPDATE =================
    public void updateVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (!vehicleDAO.existsById(vehicle.getVehicleId())) {
            throw new BusinessException("Vehicle not found");
        }

        vehicleDAO.updateVehicle(vehicle);
    }

    // ================= DELETE =================
    public void deleteVehicle(int vehicleId) {

        if (!vehicleDAO.existsById(vehicleId)) {
            throw new BusinessException("Vehicle not found");
        }

        vehicleDAO.deleteVehicle(vehicleId);
    }

    // ================= GET BY ID =================
    public Vehicle getVehicleById(int vehicleId) {

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {
            throw new BusinessException("Vehicle not found");
        }

        return vehicle;
    }

    // ================= GET BY CUSTOMER =================
    public List<Vehicle> getVehiclesByCustomerId(int customerId) {

        List<Vehicle> list = vehicleDAO.getVehiclesByCustomerId(customerId);

        if (list.isEmpty()) {
            throw new BusinessException("No vehicles found for this customer");
        }

        return list;
    }

    // ================= GET ALL =================
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.getAllVehicles();
    }

    // ================= PAGINATION =================
    public List<Vehicle> getVehiclesPaginated(int pageNumber, int pageSize) {

        if (pageNumber <= 0 || pageSize <= 0) {
            throw new BusinessException("Invalid pagination values");
        }

        return vehicleDAO.getVehiclesPaginated(pageNumber, pageSize);
    }

    public int getTotalVehicleCount() {
        return vehicleDAO.getTotalVehicleCount();
    }

    // ================= VALIDATION =================
    private void validateVehicle(Vehicle vehicle) {

        if (vehicle.getCustomerId() <= 0) {
            throw new BusinessException("Invalid customer ID");
        }

        if (vehicle.getVehicleNumber() == null || vehicle.getVehicleNumber().trim().isEmpty()) {
            throw new BusinessException("Vehicle number is required");
        }

        if (vehicle.getVehicleType() == null || vehicle.getVehicleType().trim().isEmpty()) {
            throw new BusinessException("Vehicle type is required");
        }

        if (vehicle.getBrand() == null || vehicle.getBrand().trim().isEmpty()) {
            throw new BusinessException("Brand is required");
        }

        if (vehicle.getModel() == null || vehicle.getModel().trim().isEmpty()) {
            throw new BusinessException("Model is required");
        }

        if (vehicle.getYear() < 1900 || vehicle.getYear() > 2100) {
            throw new BusinessException("Invalid vehicle year");
        }
    }
}