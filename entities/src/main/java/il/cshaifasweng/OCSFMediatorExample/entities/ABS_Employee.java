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
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.JOINED) // Use JOINED strategy for inheritance
public abstract class ABS_Employee extends ABS_Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "password", nullable = false)
    private String password;

    public ABS_Employee(int id, String name, boolean isOnline, String password) {
        super(id, name, isOnline);
        this.password = password;
    }

    public ABS_Employee() {
        super();
    }

    // Getters and setters

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
