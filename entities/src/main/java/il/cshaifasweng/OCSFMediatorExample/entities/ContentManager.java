package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;

@Entity
@Table(name = "content_manager")
public class ContentManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
