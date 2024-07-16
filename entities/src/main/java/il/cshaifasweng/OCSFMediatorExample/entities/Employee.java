package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "Employees")
@Inheritance(strategy = InheritanceType.JOINED) // Use JOINED strategy for inheritance
public class Employee extends Person
{

    public enum EmployeeType
    {
        THEATER_MANAGER,
        COMPANY_MANAGER,
        CUSTOMER_SERVICE,
        CONTENT_MANAGER,
    }

    @Column
    protected EmployeeType employeeType;

    @Column(name = "password", nullable = false)
    protected String password;

    public Employee(String id_number ,String name, boolean isOnline, String password, EmployeeType employeeType) {
        super(id_number,name, isOnline);
        this.password = password;
        this.employeeType = employeeType;
    }

    public Employee() {
        super();
    }

    // Getters and setters

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeType getEmployeeType()
    {
        return employeeType;
    }

    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject(super.toJson());
        jsonObject.put("password", password);
        jsonObject.put("employeeType", employeeType.name());
        return jsonObject.toString();
    }

    public static Employee fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Person person = Person.fromJson(jsonString);
        Employee employee = new Employee();
        employee.id = person.getId();
        employee.id_number = person.getId_number();
        employee.name = person.getName();
        employee.isOnline = person.isOnline();
        employee.password = jsonObject.getString("password");
        employee.employeeType = EmployeeType.valueOf(jsonObject.getString("employeeType"));
        return employee;
    }

}