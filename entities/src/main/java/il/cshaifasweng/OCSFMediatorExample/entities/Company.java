package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "company")
    private List<Theater> theaters;

    @OneToOne
    private CompanyManager companyManager;

    @OneToOne
    private ContentManager contentManager;

    @OneToOne(mappedBy = "company")
    private List<CustomerService> customerServices;

    @OneToMany(mappedBy = "company")
    private List<Movie> movies;

    public Company(int id, String name, List<Theater> theaters, CompanyManager companyManager, ContentManager contentManager, List<CustomerService> customerServices, List<Movie> movies) {
        this.id = id;
        this.theaters = theaters;
        this.companyManager = companyManager;
        this.contentManager = contentManager;
        this.customerServices = customerServices;
        this.movies = movies;
    }

    public Company() {

    }

    // Constructors, getters, and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<Theater> theaters) {
        this.theaters = theaters;
    }

    public CompanyManager getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public List<CustomerService> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(List<CustomerService> customerServices) {
        this.customerServices = customerServices;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
