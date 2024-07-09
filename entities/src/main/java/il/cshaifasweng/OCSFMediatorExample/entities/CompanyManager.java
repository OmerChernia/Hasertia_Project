package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class CompanyManager {
    Company company;
    List<PriceRequest> priceRequests;

    public CompanyManager(Company company, List<PriceRequest> priceRequests) {
        this.company = company;
        this.priceRequests = priceRequests;
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
