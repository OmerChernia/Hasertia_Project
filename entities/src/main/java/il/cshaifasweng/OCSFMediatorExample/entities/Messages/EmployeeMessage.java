package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

import il.cshaifasweng.OCSFMediatorExample.entities.Employee;

import java.util.ArrayList;

public class EmployeeMessage extends Message{
    public ArrayList<Employee> employees;
    public String id_number;
    public String name;
    public boolean isOnline;
    public String password;
    public Employee.EmployeeType employeeType;
    public RequestType requestType;
    public ResponseType responseType;

    public EmployeeMessage(Message.MessageType messageType, RequestType requestType , String id_number)
    {
        super();
        this.id_number = id_number;
        this.requestType = requestType;
    }
    public EmployeeMessage(Message.MessageType messageType, RequestType requestType , Employee employee , String id_number)
    {
        super();
        this.requestType = requestType;
        this.id_number = id_number;
        this.name = employee.getName();
        this.isOnline = employee.isOnline();
        this.password = employee.getPassword();
        this.employeeType = employee.getEmployeeType();
    }
    public EmployeeMessage(Message.MessageType messageType, RequestType requestType)
    {
        super();
        this.requestType = requestType;
    }

    public enum ResponseType
    {
        EMPLOYEE_ADDED,
        EMPLOYEE_REMOVED_SUCCESSFULLY,
        EMPLOYEE_REMOVED_UNSUCCESSFULLY,
        EMPLOYEE_LIST,
        EMPLOYEE_FOUND,
        EMPLOYEE_FAILED,
        EMPLOYEE_REMOVED,
        EMPLOYEE_NOT_FOUND,
    }
    public enum RequestType
    {
        ADD_EMPLOYEE,
        REMOVE_EMPLOYEE,
        GET_EMPLOYEE_BY_ID,
        GET_ALL_EMPLOYEES,
    }
}

