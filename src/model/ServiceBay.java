package model;

public class ServiceBay
{
    private int bayId;
    private String bayName;
    private BayStatus status;

    public ServiceBay(int bayId , String bayName , BayStatus status)
    {
        this.bayId = bayId;
        this.bayName = bayName;
        this.status = status;
    }
    public ServiceBay(String bayName , BayStatus status)
    {
        this.bayName = bayName;
        this.status = status;
    }
    public int getBayId()
    {
        return bayId;
    }
    public void setBayId(int bayId)
    {
        this.bayId = bayId;
    }
    public String getBayName()
    {
        return bayName;
    }
    public void setBayName(String bayName)
    {
        this.bayName = bayName;
    }
    public BayStatus getStatus()
    {
        return  status;
    }
    public void setStatus(BayStatus status)
    {
        this.status = status;
    }
    @Override
    public String toString()
    {
        return bayName + "(" + status + ")";
    }
}
