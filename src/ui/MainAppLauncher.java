package ui;
import dao.ServiceBookingDAO;
import dao.ServiceBayDAO;
import service.ServiceQueueService;
import controller.ServiceQueueController;
import javax.swing.*;
import java.awt.*;


public class MainAppLauncher extends JFrame
{
    private final ServiceBookingDAO bookingDAO = new ServiceBookingDAO();
    ServiceBayDAO bayDAO = new ServiceBayDAO();
    private final ServiceQueueService serviceQueueService = new ServiceQueueService(bookingDAO , bayDAO);
    private final ServiceQueueController serviceQueueController =
            new ServiceQueueController(serviceQueueService);
    private final ServiceQueuePanel serviceQueuePanel = new ServiceQueuePanel(serviceQueueController);

    public MainAppLauncher()
    {
        setTitle("Vehicle Service Management System");
        setSize(800 , 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(0 , 1 , 10 ,10));

//        Here , i am defining buttons

        JButton customerButton = new JButton("Customer Management");
        JButton vehicleButton = new JButton("Vehicle Management");
        JButton serviceQueueBtn = new JButton("Service Queue");
        JButton exitButton = new JButton("Exit");


//      Here , i am adding buttons to the frame.

       add(customerButton);
       add(vehicleButton);
        add(serviceQueueBtn);
        add(exitButton);

//        Action Listeners
        customerButton.addActionListener(e -> {
            new CustomerManagementFrame();
        });
        vehicleButton.addActionListener(e -> {
            new VehicleManagementFrame();
        });

        serviceQueueBtn.addActionListener(e -> {
            setContentPane(serviceQueuePanel);
            revalidate();
            repaint();
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        
            new MainAppLauncher();
    }

}
