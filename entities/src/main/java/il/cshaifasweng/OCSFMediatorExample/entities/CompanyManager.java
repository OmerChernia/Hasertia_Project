package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_manager")
public class CompanyManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Company company;

    @OneToMany(mappedBy = "companyManager")
    private List<PriceRequest> priceRequests;

    public CompanyManager(Company company, List<PriceRequest> priceRequests) {
        this.company = company;
        this.priceRequests = priceRequests;
    }

    public CompanyManager() {
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<PriceRequest> getPriceRequests() {
        return priceRequests;
    }

    public void setPriceRequests(List<PriceRequest> priceRequests) {
        this.priceRequests = priceRequests;
    }
}