package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeHandler {
    private static EmployeeMessage message;

    public EmployeeHandler(EmployeeMessage message) {
        EmployeeHandler.message = message;
    }

    public void handle() {
        switch (message.requestType) {
            case ADD_EMPLOYEE:
                addEmployee();
                break;
            case REMOVE_EMPLOYEE:
                removeEmployee();
                break;
            case GET_EMPLOYEE_BY_ID:

                break;
            case GET_ALL_EMPLOYEES:
                getAllEmployees();
                break;
            default:
                throw new IllegalArgumentException("Invalid request type: " + message.requestType);
        }
    }

    private void addEmployee() {
        // Implement the logic to add an employee
        // After adding the employee, set the responseType to EMPLOYEE_ADDED
        message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_ADDED;
    }

    private void removeEmployee() {
        // Implement the logic to remove an employee
        // After removing the employee, set the responseType to EMPLOYEE_REMOVED_SUCCESSFULLY
        message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_REMOVED_SUCCESSFULLY;
    }



    private void getAllEmployees() {
        // Implement the logic to get all employees
        // After getting all employees, set the responseType to EMPLOYEE_LIST
        message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_LIST;
    }
}