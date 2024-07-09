package il.cshaifasweng.OCSFMediatorExample.entities;

public abstract class ABS_Employee extends ABS_Person{
    String password;

    public ABS_Employee(int id, String name, boolean isOnline, String password) {
        super(id, name, isOnline);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
