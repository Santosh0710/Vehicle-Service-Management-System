package dsa_practice;
import java.util.HashMap;
public class HashingDemo
{
    public static void main(String[] args) {
        HashMap<Integer,String> vehicleMap = new HashMap<>();
        vehicleMap.put(101, "Hyundai");
        vehicleMap.put(102,"Honda");
        vehicleMap.put(101 , "Tata");

        System.out.println(vehicleMap.get(101));
    }
}
