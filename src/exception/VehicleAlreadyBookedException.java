package exception;

public class VehicleAlreadyBookedException extends RuntimeException
{
    public VehicleAlreadyBookedException(String message)
    {
        super(message);
    }
}
