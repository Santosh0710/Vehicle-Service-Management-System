package dsa_practice;

import java.util.HashMap;
import model.Vehicle;

public class VehicleLookupDemo{
    public static void main(String[] args) {
        HashMap<Integer,Vehicle> vehicleMap = new HashMap<>();
//        using your existing vehicle class
//        Vehicle v1 = new Vehicle(101, 1001 ,"Hyundai",2021);
//        Vehicle v2 = new Vehicle(102, 1002 ,"Honda",2019);
//        Vehicle v3 = new Vehicle(103, 1003 ,"Tata",2020);
//        Add Vehicles
//       vehicleMap.put(v1.getVehicleId(),v1);
//       vehicleMap.put(v2.getVehicleId(),v2);
//       vehicleMap.put(v3.getVehicleId(),v3);

//       Fast Lookup
        Vehicle found = vehicleMap.get(102);
        System.out.println("Found Vehicle:");
        found.displayInfo();

    }
}