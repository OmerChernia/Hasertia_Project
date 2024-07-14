package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Employee;

public class EmployeeLoginMessage extends LoginMessage
{
    public String password;
    public Employee.EmployeeType employeeType;

    public EmployeeLoginMessage(String id, MessageType type, RequestType requestType, String password)
    {
        // LOGIN , LOGOUT
        super(id, type, requestType);
        this.password = password;
    }
}
