package dsa_practice;

import java.util.LinkedList;
public class ServiceQueueDemo
{
    public static void main(String[] args) {
        LinkedList<String> serviceQueue = new LinkedList<>();
//        vehicles entering service queue
        serviceQueue.add("Vehicle-101");
        serviceQueue.add("Vehicle-102");
        serviceQueue.add("Vehicle-103");

        System.out.println("Initial Queue: "+ serviceQueue);

//        First Vehicle Serviced
        String serviced = serviceQueue.removeFirst();
        System.out.println("Serviced: "+serviced);

        System.out.println("Remaining Queue: "+ serviceQueue);
    }
}
