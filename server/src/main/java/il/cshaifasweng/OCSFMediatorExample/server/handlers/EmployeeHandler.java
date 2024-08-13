package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EmployeeHandler {
    private static EmployeeMessage message;

    public EmployeeHandler(EmployeeMessage message) {
        this.message = message;
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
                getEmployeeById();
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

    public static int getEmployeeById() {
        // Create a SessionFactory and a Session
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            // Start a transaction
            session.beginTransaction();

            // Get the Employee entity from the database using the id_number
            Employee employee = session.get(Employee.class, message.id_number);

            // If the Employee entity exists, set the responseType to EMPLOYEE_FOUND and add the Employee to the employees list in the message
            if (employee != null) {
                message.employees.add(employee);
                message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_FOUND;

                // Return the id of the Employee entity
                return employee.getId();
            } else {
                // If the Employee entity does not exist, set the responseType to EMPLOYEE_NOT_FOUND
                message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_NOT_FOUND;
            }

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } finally {
            // Close the Session
            session.close();
        }
        return 0;
    }

    private void getAllEmployees() {
        // Implement the logic to get all employees
        // After getting all employees, set the responseType to EMPLOYEE_LIST
        message.responseType = EmployeeMessage.ResponseType.EMPLOYEE_LIST;
    }
}