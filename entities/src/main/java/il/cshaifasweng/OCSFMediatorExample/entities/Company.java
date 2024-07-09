package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class Company {
    int id;
    String name;
    List<Theater> theaters;
    CompanyManager companyManager;
    ContentManager contentManager;
    List<CustomerService> customerServices;
    List<Movie> movies;

    public Company(int id, String name, List<Theater> theaters, CompanyManager companyManager, ContentManager contentManager, List<CustomerService> customerServices, List<Movie> movies) {
        this.id = id;
        this.name = name;
        this.theaters = theaters;
        this.companyManager = companyManager;
        this.contentManager = contentManager;
        this.customerServices = customerServices;
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
