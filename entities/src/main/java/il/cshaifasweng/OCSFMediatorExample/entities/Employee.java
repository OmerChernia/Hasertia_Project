package il.cshaifasweng.OCSFMediatorExample.entities;

import com.google.gson.Gson;
import org.json.JSONObject;

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

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getId());
        jsonObject.put("id_number", getId_number());
        jsonObject.put("name", getName());
        jsonObject.put("isOnline", isOnline());
        jsonObject.put("password", password);
        jsonObject.put("employeeType", employeeType.name());
        return jsonObject.toString();
    }

    public static Employee fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        Employee employee = new Employee();
        employee.setId(jsonObject.getInt("id"));
        employee.setId_number(jsonObject.getString("id_number"));
        employee.setName(jsonObject.getString("name"));
        employee.setOnline(jsonObject.getBoolean("isOnline"));
        employee.setPassword(jsonObject.getString("password"));
        employee.employeeType = EmployeeType.valueOf(jsonObject.getString("employeeType"));
        return employee;
    }

}