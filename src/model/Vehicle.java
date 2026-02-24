
package model;

public class Vehicle {

    private int vehicleId;
    private int customerId;
    private String vehicleNumber;   // NEW (VERY IMPORTANT)
    private String vehicleType;     // Car / Bike
    private String brand;
    private String model;           // NEW
    private int year;

    // Default constructor
    public Vehicle() {}

    // Parameterized constructor
    public Vehicle(int vehicleId, int customerId, String vehicleNumber,
                   String vehicleType, String brand, String model, int year) {

        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // Getters & Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    // equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle other = (Vehicle) obj;
        return this.vehicleId == other.vehicleId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(vehicleId);
    }

    public void displayInfo() {
        System.out.println("Vehicle Id: " + vehicleId);
        System.out.println("Customer Id: " + customerId);
        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Type: " + vehicleType);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
    }
}