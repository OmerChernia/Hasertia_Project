package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

public class EmployeeLoginMessage extends LoginMessage
{
    public String password;
    public EmployeeType employeeType;

    public EmployeeLoginMessage(String id, MessageType type, RequestType requestType, String password)
    {
        // LOGIN , LOGOUT
        super(id, type, requestType);
        this.password = password;
    }
    public enum EmployeeType
    {
        THEATER_MANAGER,
        COMPANY_MANAGER,
        CUSTOMER_SERVICE,
        CONTENT_MANAGER
    }
}
