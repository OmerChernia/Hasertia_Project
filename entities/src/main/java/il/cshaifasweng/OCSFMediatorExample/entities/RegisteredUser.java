package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registered_users")
public class RegisteredUser extends Person {
    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    private int ticket_counter;

    public RegisteredUser() {
    }


    public RegisteredUser(String id_number ,String name, boolean isOnline, String email,int ticket_counter) {
        super(id_number ,name, isOnline);
        this.email = email;
        this.ticket_counter = ticket_counter;
    }
    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTicket_counter() {
        return ticket_counter;
    }

    public void setTicket_counter(int ticket_counter) {
        this.ticket_counter = ticket_counter;
    }

}