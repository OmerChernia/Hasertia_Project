package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

public class EmployeeLoginMessage extends LoginMessage
{
    String password;

    public EmployeeLoginMessage(String id, MessageType type, RequestType requestType, String password)
    {
        super(id, type, requestType);
        this.password = password;
    }
}
