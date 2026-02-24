package dsa_practice;

import java.util.TreeMap;
import java.util.Map;

public class ServiceRecordDemo{
    public static void main(String[] args) {
        TreeMap<Integer,String> serviceRecords = new TreeMap<>();

        serviceRecords.put(1003, "Tata: 2800");
        serviceRecords.put(1001,"Hyundai : 3000");
        serviceRecords.put(1002,"Honda : 4500");

        System.out.println("---Sorted Service Records---");
        for(Map.Entry<Integer,String> entry : serviceRecords.entrySet()){
            System.out.println("Bill "+ entry.getKey() + "--> "+ entry.getValue());
        }
    }
}