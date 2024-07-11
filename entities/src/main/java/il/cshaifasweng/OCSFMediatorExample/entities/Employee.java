package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

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
    private EmployeeType employeeType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "password", nullable = false)
    private String password;

    public Employee(String name, boolean isOnline, String password, EmployeeType employeeType) {
        super(name, isOnline);
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
}