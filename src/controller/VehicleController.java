package controller;

import model.Vehicle;
import service.VehicleService;
import exception.BusinessException;
import exception.DatabaseException;

import java.util.List;

public class VehicleController {

    private VehicleService vehicleService = new VehicleService();

    // ================= ADD =================
    public int addVehicle(Vehicle vehicle) {
        try {
            return vehicleService.addVehicle(vehicle);
        } catch (BusinessException | DatabaseException e) {
            throw e; // pass to UI
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while adding vehicle", e);
        }
    }

    // ================= UPDATE =================
    public void updateVehicle(Vehicle vehicle) {
        try {
            vehicleService.updateVehicle(vehicle);
        } catch (BusinessException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while updating vehicle", e);
        }
    }

    // ================= DELETE =================
    public void deleteVehicle(int vehicleId) {
        try {
            vehicleService.deleteVehicle(vehicleId);
        } catch (BusinessException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while deleting vehicle", e);
        }
    }

    // ================= GET BY ID =================
    public Vehicle getVehicleById(int vehicleId) {
        try {
            return vehicleService.getVehicleById(vehicleId);
        } catch (BusinessException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching vehicle", e);
        }
    }

    // ================= GET BY CUSTOMER =================
    public List<Vehicle> getVehiclesByCustomerId(int customerId) {
        try {
            return vehicleService.getVehiclesByCustomerId(customerId);
        } catch (BusinessException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching vehicles", e);
        }
    }

    // ================= GET ALL =================
    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleService.getAllVehicles();
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching vehicles", e);
        }
    }

    // ================= PAGINATION =================
    public List<Vehicle> getVehiclesPaginated(int pageNumber, int pageSize) {
        try {
            return vehicleService.getVehiclesPaginated(pageNumber, pageSize);
        } catch (BusinessException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching vehicles", e);
        }
    }

    public int getTotalVehicleCount() {
        try {
            return vehicleService.getTotalVehicleCount();
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while counting vehicles", e);
        }
    }
}